package com.hdw.upms.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.hdw.upms.shiro.cache.RedisCacheManager;
import com.hdw.upms.shiro.cache.RedisSessionDAO;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author TuMinglong
 * @version 1.0.0
 * @Description Apache Shiro配置类
 * @date 2018年5月14日下午7:57:14
 */
@Configuration
public class ShiroConfig {

    @Value("${upms.loginUrl}")
    private String loginUrl;

    @Value("${upms.successUrl}")
    private String successUrl;

    @Value("${upms.unauthorizedUrl}")
    private String unauthorizedUrl;


    @Autowired
    private RedisSessionDAO sessionDAO;

    @Bean
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager();
    }

    // 注入异常处理类
    @Bean
    public ShiroExceptionResolver shiroExceptionResolver() {
        return new ShiroExceptionResolver();
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filtersMap = shiroFilterFactoryBean.getFilters();
        filtersMap.put("user" , ajaxSessionFilter());

        // 实现自己规则roles,这是为了实现or的效果
        // RoleFilter roleFilter = new RoleFilter();
        // filtersMap.put("roles", roleFilter);
        shiroFilterFactoryBean.setFilters(filtersMap);
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        filterChainDefinitionMap.put("/logout" , "logout");
        // 配置记住我或认证通过可以访问的地址
        filterChainDefinitionMap.put("/index" , "user");
        filterChainDefinitionMap.put("/" , "user");
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
        filterChainDefinitionMap.put("/doc.html" , "anon");

        // 其他的
        filterChainDefinitionMap.put("/test/**" , "anon");
        filterChainDefinitionMap.put("/login" , "anon");
        filterChainDefinitionMap.put("/**" , "authc");

        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl(successUrl);

        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

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

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(shiroDBRealm());
        // 注入Session管理器
        securityManager.setSessionManager(sessionManager());
        // 注入缓存管理器
        securityManager.setCacheManager(redisCacheManager());
        // 注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
     */
    @Bean
    public ShiroDBRealm shiroDBRealm() {
        ShiroDBRealm shiroDBRealm = new ShiroDBRealm();
        shiroDBRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        shiroDBRealm.setCacheManager(redisCacheManager());
        // 启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        shiroDBRealm.setAuthenticationCachingEnabled(true);
        // 缓存AuthenticationInfo信息的缓存名称
        shiroDBRealm.setAuthenticationCacheName("authenticationCache");
        // 缓存AuthorizationInfo信息的缓存名称
        shiroDBRealm.setAuthorizationCacheName("authorizationCache");
        return shiroDBRealm;
    }

    /**
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码; @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);// 散列的次数，比如散列两次，相当于md5(md5(""));
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);// 表示是否存储散列后的密码为16进制，需要和生成密码时的一样，默认是base64；
        return hashedCredentialsMatcher;
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
     * cookie对象;
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        // 记住我cookie生效时间7天 ,单位秒
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
        cookieRememberMeManager.setCipherKey(Base64.decode("5aaC5qKm5oqA5pyvAAAAAA=="));
        return cookieRememberMeManager;
    }

    @Bean(name = "sessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1 * 1 * 1000);
        sessionManager.setSessionDAO(sessionDAO);
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
        sessionManager.getSessionIdCookie().setHttpOnly(true);
        return sessionManager;
    }

    @Bean(name = "sessionValidationScheduler")
    public ExecutorServiceSessionValidationScheduler getExecutorServiceSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setInterval(900000);
        return scheduler;
    }

    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

}
