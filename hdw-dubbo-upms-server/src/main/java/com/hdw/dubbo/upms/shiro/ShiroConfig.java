package com.hdw.dubbo.upms.shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hdw.dubbo.upms.shiro.cache.ShiroSpringCacheManager;
import com.hdw.dubbo.upms.shiro.captcha.DreamCaptcha;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

import javax.servlet.Filter;

/**
 * 
 * @description Shiro配置
 * @author TuMinglong
 * @date 2018年3月5日 上午11:22:21
 */
@Configuration
public class ShiroConfig {

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
		securityManager.setRealm(shiroDbRealm());
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
	public ShiroDbRealm shiroDbRealm() {
		ShiroDbRealm shiroDbRealm = new ShiroDbRealm();
		shiroDbRealm.setCacheManager(shiroSpringCacheManager());
		shiroDbRealm.setCredentialsMatcher(credentialsMatcher());
		// 启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
		shiroDbRealm.setAuthenticationCachingEnabled(true);
		// 缓存AuthenticationInfo信息的缓存名称
		shiroDbRealm.setAuthenticationCacheName("authenticationCache");
		// 缓存AuthorizationInfo信息的缓存名称
		shiroDbRealm.setAuthorizationCacheName("authorizationCache");

		return shiroDbRealm;
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
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/index");
		// 未授权界面
		shiroFilterFactoryBean.setUnauthorizedUrl("/unauth");
		// 拦截器
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");
		// 配置记住我或认证通过可以访问的地址
		filterChainDefinitionMap.put("/index", "user");
		filterChainDefinitionMap.put("/", "user");
		filterChainDefinitionMap.put("/login", "anon");

		/**
		 * 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 。
		 * anon 不需要认证
		 * authc 需要认证
		 * user 验证通过或RememberMe登录的都可以
		 */
		// 开放的静态资源
		filterChainDefinitionMap.put("/favicon.ico", "anon");// 网站图标
		filterChainDefinitionMap.put("/static/**", "anon");// 配置static文件下资源能被访问的
		filterChainDefinitionMap.put("/kaptcha.jpg", "anon");// 图片验证码(kaptcha框架)
		filterChainDefinitionMap.put("/api/v1/**", "anon");// API接口

		// swagger接口文档
		filterChainDefinitionMap.put("/v2/api-docs", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		filterChainDefinitionMap.put("/swagger-resources/**", "anon");
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");

		// 其他的
		filterChainDefinitionMap.put("/**", "authc");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
		filters.put("user", ajaxSessionFilter());
		shiroFilterFactoryBean.setFilters(filters);

		return shiroFilterFactoryBean;
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
		passwordHash.setHashIterations(1);
		return passwordHash;
	}

	/**
	 * 密码错误5次锁定半小时
	 * 
	 * @return
	 */
	@Bean(name = "credentialsMatcher")
	public RetryLimitCredentialsMatcher credentialsMatcher() {
		RetryLimitCredentialsMatcher credentialsMatcher = new RetryLimitCredentialsMatcher(shiroSpringCacheManager());
		credentialsMatcher.setRetryLimitCacheName("halfHour");
		credentialsMatcher.setPasswordHash(passwordHash());
		return credentialsMatcher;
	}

	@Bean(name = "shiroDialect")
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}
}
