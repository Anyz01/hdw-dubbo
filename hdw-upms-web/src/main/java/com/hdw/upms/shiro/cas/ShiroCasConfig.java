package com.hdw.upms.shiro.cas;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.hdw.upms.shiro.ShiroAjaxSessionFilter;
import com.hdw.upms.shiro.cache.RedisCacheManager;
import com.hdw.upms.shiro.cache.RedisSessionDAO;
import io.buji.pac4j.filter.CallbackFilter;
import io.buji.pac4j.filter.LogoutFilter;
import io.buji.pac4j.filter.SecurityFilter;
import io.buji.pac4j.subject.Pac4jSubjectFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
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
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author TuMinglong
 * @description Shiro CAS 配置
 * @date 2018年3月5日 上午11:22:21
 */
//@Configuration
public class ShiroCasConfig {

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
    private JwtAuthenticator jwtAuthenticator;

    @Autowired
    private RedisSessionDAO sessionDAO;

    @Bean
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager();
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置自定义Realm
        securityManager.setRealm(pac4jDbRealm());
        securityManager.setSubjectFactory(pac4jSubjectFactory());
        // 注入缓存管理器
        securityManager.setCacheManager(redisCacheManager());
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
        ShiroCasRealm pac4jDbRealm = new ShiroCasRealm();
        return pac4jDbRealm;
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
        filterChainDefinitionMap.put("/favicon.ico" , "anon");// 网站图标
        filterChainDefinitionMap.put("/static/**" , "anon");// 配置static文件下资源能被访问
        filterChainDefinitionMap.put("/css/**" , "anon");
        filterChainDefinitionMap.put("/font/**" , "anon");
        filterChainDefinitionMap.put("/img/**" , "anon");
        filterChainDefinitionMap.put("/js/**" , "anon");
        filterChainDefinitionMap.put("/plugins/**" , "anon");
        filterChainDefinitionMap.put("/kaptcha.jpg" , "anon");// 图片验证码(kaptcha框架)
        filterChainDefinitionMap.put("/xlsFile/**" , "anon");
        filterChainDefinitionMap.put("/upload/**" , "anon");
        filterChainDefinitionMap.put("/api/**" , "anon");// API接口

        // swagger接口文档
        filterChainDefinitionMap.put("/v2/api-docs" , "anon");
        filterChainDefinitionMap.put("/webjars/**" , "anon");
        filterChainDefinitionMap.put("/swagger-resources/**" , "anon");
        filterChainDefinitionMap.put("/swagger-ui.html" , "anon");

        // 其他的
        filterChainDefinitionMap.put("/callback" , "callbackFilter");
        filterChainDefinitionMap.put("/logout" , "logoutFilter");
        filterChainDefinitionMap.put("/**" , "casSecurityFilter");

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
        filters.put("casSecurityFilter" , casSecurityFilter());
        CallbackFilter callbackFilter = new CallbackFilter();
        callbackFilter.setConfig(casConfig());
        filters.put("callbackFilter" , callbackFilter);

        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setConfig(casConfig());
        logoutFilter.setCentralLogout(true);
        logoutFilter.setDefaultUrl(serviceUrl);
        filters.put("logoutFilter" , logoutFilter);
        filters.put("user" , ajaxSessionFilter());
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
     * 开启shiro aop注解支持. 使用代理方式; 所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * cookie对象
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // 记住我cookie生效时间1小时 ,单位秒
        simpleCookie.setMaxAge(60 * 60 * 1 * 1);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    @Bean(name = "sessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1 * 1 * 1000);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setCacheManager(redisCacheManager());
        // url中是否显示session Id
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        // 删除失效的session
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationInterval(60 * 60 * 1 * 1 * 1000);
        sessionManager.setSessionValidationScheduler(getExecutorServiceSessionValidationScheduler());

        sessionManager.getSessionIdCookie().setName("session-z-id");
        sessionManager.getSessionIdCookie().setPath("/hdw");
        sessionManager.getSessionIdCookie().setMaxAge(60 * 60 * 1 * 1);
        return sessionManager;
    }

    @Bean(name = "sessionValidationScheduler")
    public ExecutorServiceSessionValidationScheduler getExecutorServiceSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setInterval(60 * 60 * 1 * 1);
        return scheduler;
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
     * 通过rest接口可以获取tgt,获取service ticket,甚至可以获取casProfile
     *
     * @return
     */
    @Bean
    public CasRestFormClient casRestFormClient() {
        CasRestFormClient casRestFormClient = new CasRestFormClient();
        casRestFormClient.setConfiguration(casConfiguration());
        casRestFormClient.setName("rest");
        return casRestFormClient;

    }

    @Bean
    public Clients clients() {
        // 设置默认client
        Clients clients = new Clients();
        // token校验器，可以用HeaderClient更安全
        ParameterClient parameterClient = new ParameterClient("token" , jwtAuthenticator);
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


}
