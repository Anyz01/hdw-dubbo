package com.hdw.dubbo.common.config.redis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;



/**
 * 
 * @description Redis配置
 * @author TuMinglong
 * @date 2018年1月25日 上午10:40:31
 */
@Configuration
@EnableCaching 
public class RedisConfig extends CachingConfigurerSupport{
	
	protected final static Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
	@Value("${spring.redis.cluster.flag}")
	private boolean redisClusterFlag;
	
    @Autowired 
    ClusterConfigurationProperties clusterProperties;
    
    @Bean
    public  RedisConnectionFactory connectionFactory() {
    	if(redisClusterFlag) {
    		return new JedisConnectionFactory(
    	            new RedisClusterConfiguration(clusterProperties.getNodes()),new JedisPoolConfig());
    	}else {
    		return new JedisConnectionFactory(new JedisPoolConfig());
    	}
    }
    
    
    @Bean
    public RedisTemplate<Serializable, Serializable> redisTemplate(
    		RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Serializable, Serializable> redisTemplate = new RedisTemplate<Serializable, Serializable>();
        //key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
        //所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现 ObjectRedisSerializer
        //或者JdkSerializationRedisSerializer序列化方式;
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        //以上4条配置可以不用
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        
        return redisTemplate;
    }
    
    /**
     * 缓存管理器.
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<?,?> redisTemplate) {
    	RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        rcm.setDefaultExpiration(600);
        rcm.setUsePrefix(true);
   
        Map<String,Long> expires=new HashMap<String, Long>();
        expires.put("halfHour", 1800l);
        expires.put("hour", 3600l);
        expires.put("oneDay", 86400l);
        
        //shiro cache keys
        expires.put("authorizationCache", 1800l);
        expires.put("authenticationCache", 1800l);
        expires.put("activeSessionCache", 1800l);
        expires.put("pac4jAuthorizationCache", 1800l);
        expires.put("pac4jAuthenticationCache", 1800l);
        
        rcm.setExpires(expires);
        
       return rcm;
    }
    

}
