package com.hdw.dubbo.upms.shiro;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.config.CasProtocol;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.jwt.profile.JwtGenerator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hdw.dubbo.upms.shiro.cache.ShiroSpringCacheManager;
import com.hdw.dubbo.upms.shiro.captcha.DreamCaptcha;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import io.buji.pac4j.filter.CallbackFilter;
import io.buji.pac4j.filter.LogoutFilter;
import io.buji.pac4j.filter.SecurityFilter;
import io.buji.pac4j.subject.Pac4jSubjectFactory;

import javax.servlet.Filter;

/**
 * 
 * @description Shiro配置
 * @author TuMinglong
 * @date 2018年3月5日 上午11:22:21
 */
@Configuration
public class ShiroConfig2 {

	@Value("${sso.cas.server.loginUrl}")
	private String loginUrl;

	@Value("${sso.cas.server.prefixUrl}")
	private String prefixUrl;

	@Value("${sso.cas.client.callbackUrl}")
	private String callbackUrl;

	@Value("${sso.cas.serviceUrl}")
	private String serviceUrl;

	@Value("${jwt.salt}")
	private String salt;

	@Autowired
	private CacheManager cacheManager;

	/**
	 * 验证码
	 * 
	 * @return
	 */
	@Bean
	public DreamCaptcha dreamCaptcha() {
		DreamCaptcha dreamCaptcha = new DreamCaptcha();
		dreamCaptcha.setCacheManager(shiroSpringCacheManager());
		// 复用半小时缓存
		dreamCaptcha.setCacheName("halfHour");
		return dreamCaptcha;
	}

	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置自定义Realm
		securityManager.setRealm(pac4jDbRealm());
		securityManager.setSubjectFactory(pac4jSubjectFactory());
		// 注入缓存管理器
		securityManager.setCacheManager(shiroSpringCacheManager());
		// 记住密码管理
		securityManager.setRememberMeManager(rememberMeManager());
		// session管理
		securityManager.setSessionManager(sessionManager());

		return securityManager;
	}

	/**
	 * 身份认证realm(账号密码校验；权限等)
	 */
	@Bean
	public Realm pac4jDbRealm() {
		Pac4jDbRealm pac4jDbRealm = new Pac4jDbRealm();
		return pac4jDbRealm;
	}

	/**
	 * 记住密码Cookie
	 * 
	 * @return
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		simpleCookie.setHttpOnly(true);
		// 7天
		simpleCookie.setMaxAge(7 * 24 * 60 * 60);
		return simpleCookie;
	}

	/**
	 * rememberMe管理器
	 * 
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		cookieRememberMeManager.setCipherKey(org.apache.shiro.codec.Base64.decode("5aaC5qKm5oqA5pyvAAAAAA=="));
		return cookieRememberMeManager;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setFilters(filters());
		// 拦截器
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

		/**
		 * 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 。 anon 不需要认证 authc 需要认证 user
		 * 验证通过或RememberMe登录的都可以
		 */
		// 开放的静态资源
		filterChainDefinitionMap.put("/favicon.ico", "anon");// 网站图标
		filterChainDefinitionMap.put("/static/**", "anon");// 配置static文件下资源能被访问的
		filterChainDefinitionMap.put("/kaptcha.jpg", "anon");// 图片验证码(kaptcha框架)
		filterChainDefinitionMap.put("/api/**", "anon");// API接口
		filterChainDefinitionMap.put("/solr/**", "anon");

		// swagger接口文档
		filterChainDefinitionMap.put("/v2/api-docs", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		filterChainDefinitionMap.put("/swagger-resources/**", "anon");
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");

		// 其他的
		filterChainDefinitionMap.put("/callback", "callbackFilter");
		filterChainDefinitionMap.put("/logout", "logoutFilter");
		filterChainDefinitionMap.put("/**", "casSecurityFilter");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilterFactoryBean;
	}

	/**
	 * 对shiro的过滤策略进行明确
	 * 
	 * @return
	 */
	@Bean
	protected Map<String, Filter> filters() {
		// 过滤器设置
		Map<String, Filter> filters = new HashMap<>();
		filters.put("casSecurityFilter", casSecurityFilter());
		CallbackFilter callbackFilter = new CallbackFilter();
		callbackFilter.setConfig(casConfig());
		filters.put("callbackFilter", callbackFilter);

		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setConfig(casConfig());
		logoutFilter.setCentralLogout(true);
		logoutFilter.setDefaultUrl(serviceUrl);
		filters.put("logoutFilter", logoutFilter);
		filters.put("user", ajaxSessionFilter());
		return filters;
	}

	/**
	 * ajax session超时时处理
	 * 
	 * @return
	 */
	@Bean
	public ShiroAjaxSessionFilter ajaxSessionFilter() {
		ShiroAjaxSessionFilter shiroAjaxSessionFilter = new ShiroAjaxSessionFilter();
		return shiroAjaxSessionFilter;
	}

	/**
	 * 用户授权信息Cache, 采用spring-cache
	 * 
	 * @param cacheManager
	 * @return
	 */
	@Bean
	public ShiroSpringCacheManager shiroSpringCacheManager() {
		ShiroSpringCacheManager shiroSpringCacheManager = new ShiroSpringCacheManager();
		shiroSpringCacheManager.setCacheManager(cacheManager);
		return shiroSpringCacheManager;
	}

	/**
	 * 会话DAO 用于会话的CRUD
	 * 
	 * @return
	 */
	@Bean(name = "sessionDAO")
	public EnterpriseCacheSessionDAO sessionDAO() {
		EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
		// Session缓存名字，默认就是shiro-activeSessionCache
		sessionDAO.setActiveSessionsCacheName("activeSessionCache");
		sessionDAO.setCacheManager(shiroSpringCacheManager());
		return sessionDAO;
	}

	/**
	 * 会话管理器
	 * 
	 * @return
	 */
	@Bean(name = "sessionManager")
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 设置全局会话超时时间 半小时
		sessionManager.setGlobalSessionTimeout(1800000);
		// url上带sessionId 默认为true
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		sessionManager.setSessionDAO(sessionDAO());
		// 删除过期的session
		sessionManager.setDeleteInvalidSessions(true);
		// 是否定时检查session
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionValidationInterval(1800000);

		return sessionManager;
	}

	/**
	 * shiro密码加密配置
	 * 
	 * @return
	 */
	@Bean(name = "passwordHash")
	public PasswordHash passwordHash() {
		PasswordHash passwordHash = new PasswordHash();
		// 密码加密 1次md5,增强密码可修改此处
		passwordHash.setAlgorithmName("md5");
		passwordHash.setHashIterations(2);
		return passwordHash;
	}

	@Bean(name = "shiroDialect")
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

	/**
	 * cas服务端配置
	 * 
	 * @return
	 */
	@Bean
	public CasConfiguration casConfiguration() {
		CasConfiguration casConfiguration = new CasConfiguration(loginUrl);
		casConfiguration.setProtocol(CasProtocol.CAS30);
		casConfiguration.setPrefixUrl(prefixUrl);
		return casConfiguration;
	}

	/**
	 * cas客户端配置
	 * 
	 * @return
	 */
	@Bean
	public CasClient casClient() {
		CasClient casClient = new CasClient();
		casClient.setConfiguration(casConfiguration());
		casClient.setCallbackUrl(callbackUrl);
		casClient.setName("cas");
		return casClient;
	}

	/**
	 * JWT Token 生成器，对CommonProfile生成然后每次携带token访问
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Bean
	protected JwtGenerator jwtGenerator() {
		return new JwtGenerator(new SecretSignatureConfiguration(salt), new SecretEncryptionConfiguration(salt));
	}

	/**
	 * JWT校验器，也就是目前设置的ParameterClient进行的校验器，是rest/或者前后端分离的核心校验器
	 * 
	 * @return
	 */
	@Bean
	protected JwtAuthenticator jwtAuthenticator() {
		JwtAuthenticator jwtAuthenticator = new JwtAuthenticator();
		jwtAuthenticator.addSignatureConfiguration(new SecretSignatureConfiguration(salt));
		jwtAuthenticator.addEncryptionConfiguration(new SecretEncryptionConfiguration(salt));
		return jwtAuthenticator;
	}

	/**
	 * 通过rest接口可以获取tgt,获取service ticket,甚至可以获取casProfile
	 * 
	 * @return
	 */
	@Bean
	protected CasRestFormClient casRestFormClient() {
		CasRestFormClient casRestFormClient = new CasRestFormClient();
		casRestFormClient.setConfiguration(casConfiguration());
		casRestFormClient.setName("rest");
		return casRestFormClient;

	}

	@Bean
	protected Clients clients() {
		// 设置默认client
		Clients clients = new Clients();
		// token校验器，可以用HeaderClient更安全
		ParameterClient parameterClient = new ParameterClient("token", jwtAuthenticator());
		parameterClient.setSupportGetRequest(true);
		parameterClient.setName("jwt");
		// 支持的client全部设置进去
		clients.setClients(casClient(), casRestFormClient(), parameterClient);
		return clients;
	}

	@Bean
	public Config casConfig() {
		Config config = new Config();
		config.setClients(clients());
		return config;
	}

	@Bean
	public SubjectFactory pac4jSubjectFactory() {
		return new Pac4jSubjectFactory();
	}

	/**
	 * cas核心过滤器，把支持的client写上，filter过滤时才会处理，clients必须在casConfig.clients已经注册
	 *
	 * @return
	 */
	@Bean
	public Filter casSecurityFilter() {
		SecurityFilter filter = new SecurityFilter();
		filter.setClients("cas,rest,jwt");
		filter.setConfig(casConfig());
		return filter;
	}

	/**
	 * 启用shrio 控制器授权注解拦截方式
	 * 
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
		aasa.setSecurityManager(securityManager());
		return aasa;
	}

	/**
	 * AOP式方法级权限检查
	 * 
	 * @return
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

}
