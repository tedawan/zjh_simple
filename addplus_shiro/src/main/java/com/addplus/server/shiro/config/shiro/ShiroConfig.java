package com.addplus.server.shiro.config.shiro;
import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.connector.redis.RedisSlaveTemplateManager;
import com.addplus.server.shiro.config.sessioncluster.*;
import com.addplus.server.shiro.filter.AddplusFormAuthenticationFilter;
import com.addplus.server.shiro.filter.AddplusPermissionsAuthorizationFilter;
import com.addplus.server.shiro.filter.AddplusRolesAuthorizationFilter;
import com.addplus.server.shiro.filter.BasicFilters;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Value("${spring.redis.nodes}")
    private String clusterNodes;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.max-redirects}")
    private int redirects;

    @Value("${spring.redis.expire}")
    private int expire;

    /**
     * 设置shrio的subject管理器
     */
    @Bean(value = "securityManager")
    public SecurityManager securityManager(RedisCacheManager cacheManager, ShiroSessionManager sessionManager, AdminLoginShiroRealm adminLoginShiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm.
        securityManager.setRealm(adminLoginShiroRealm);
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager);
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * 设置shiro拦截器管理
     */
    @Bean(name = "filterChainManager")
    public CustomDefaultFilterChainManager getFilterChainManager() {
        CustomDefaultFilterChainManager manager = new CustomDefaultFilterChainManager();
        //设置基本请求地址
        manager.setLoginUrl(ErrorCode.SYS_LOGIN_UNLOGIN.getCode());
        manager.setSuccessUrl(ErrorCode.SYS_SUCCESS.getCode());
        manager.setUnauthorizedUrl(ErrorCode.SYS_LOGIN_UNAUTHORITY.getCode());
        //设置基本拦截器
        Map<String, Filter> filternMap = new LinkedHashMap<String, Filter>();
        filternMap.put("perms", new AddplusPermissionsAuthorizationFilter());
        filternMap.put("authc", new AddplusFormAuthenticationFilter());
        filternMap.put("roles", new AddplusRolesAuthorizationFilter());
        filternMap.put("basicFilter", new BasicFilters());
        manager.setCustomFilters(filternMap);
        //设置基本拦截器链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        manager.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return manager;
    }

    @Bean(name = "otherfilterChainManager")
    public CustomDefaultFilterChainManager getOtherFilterChainManager() {
        CustomDefaultFilterChainManager manager = new CustomDefaultFilterChainManager();
        //设置基本请求地址
        manager.setLoginUrl(ErrorCode.SYS_LOGIN_UNLOGIN.getCode());
        manager.setSuccessUrl(ErrorCode.SYS_SUCCESS.getCode());
        manager.setUnauthorizedUrl(ErrorCode.SYS_LOGIN_UNAUTHORITY.getCode());
        //设置基本拦截器
        Map<String, Filter> filternMap = new LinkedHashMap<String, Filter>();
        filternMap.put("perms", new AddplusPermissionsAuthorizationFilter());
        filternMap.put("authc", new AddplusFormAuthenticationFilter());
        filternMap.put("roles", new AddplusRolesAuthorizationFilter());
        filternMap.put("basicFilter", new BasicFilters());
        manager.setCustomFilters(filternMap);
        //设置基本拦截器链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        manager.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return manager;
    }

    /**
     * 自定义拦截器匹配规则处理
     */
    @Bean(value = "filterChainResolver")
    public CustomPathMatchingFilterChainResolver getFilterChainResolver(CustomDefaultFilterChainManager filterChainManager) {
        CustomPathMatchingFilterChainResolver resolver = new CustomPathMatchingFilterChainResolver();
        resolver.setCustomDefaultFilterChainManager(filterChainManager);
        return resolver;
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, CustomPathMatchingFilterChainResolver getFilterChainResolver) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        try {
            ((AbstractShiroFilter) shiroFilterFactoryBean.getObject()).setFilterChainResolver(getFilterChainResolver);
        } catch (Exception e) {
            logger.error("Register FilterChainResolver fail . " + e.getMessage());
        }
        return shiroFilterFactoryBean;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager(RedisTemplate redisTemplate, RedisSlaveTemplateManager redisSlaveTemplateManager) {
        RedisManager redisManager = new RedisManager(redisTemplate, redisSlaveTemplateManager);
        redisManager.setExpire(expire);// 配置缓存过期时间
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        return redisCacheManager;
    }


    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 基于shiro-redis开源插件修改为集群版
     */
    @Bean
    public RedisSessionDAO redisSessionDAO(RedisManager redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        return redisSessionDAO;
    }

    /**
     * shiro session的管理
     */
    @Bean
    public ShiroSessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     *
     * @return
     */
    @Bean
    public CredentialsMatcherRdis hashedCredentialsMatcher() {
        CredentialsMatcherRdis hashedCredentialsMatcher = new CredentialsMatcherRdis();
        //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    @Bean(name = "adminLoginShiroRealm")
    public AdminLoginShiroRealm adminLoginShiroRealm() {
        AdminLoginShiroRealm baseShiroRealm = new AdminLoginShiroRealm();
        baseShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        baseShiroRealm.setName(LoginType.ADMIN.getName());
        return baseShiroRealm;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
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
     * Shiro生命周期处理器
     */
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    @Bean
    public DefaultHashService defaultHashService() {
        DefaultHashService hashService = new DefaultHashService(); //默认算法SHA-512
        hashService.setHashAlgorithmName("md5");
        hashService.setHashIterations(2); //生成Hash值的迭代次数
        return hashService;
    }
}
