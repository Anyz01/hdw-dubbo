package com.hdw.common.config.redis;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    
    @Primary
    @Bean(name="redisTemplate")
    public RedisTemplate<Serializable,Serializable> redisTemplate(
    		RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Serializable,Serializable> redisTemplate = new RedisTemplate<Serializable,Serializable>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer=new JdkSerializationRedisSerializer();
        redisTemplate.setKeySerializer(jdkSerializationRedisSerializer);
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashKeySerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        return redisTemplate;
    }
    
    @Bean(name="stringRedisTemplate")
    public RedisTemplate<String, Object> stringRedisTemplate(
    		RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }
    
    @Bean(name="longRedisTemplate")
    public RedisTemplate<String, Object> longRedisTemplate(
    		RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        GenericToStringSerializer<Long> genericToStringSerializer=new GenericToStringSerializer<>(Long.class);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(genericToStringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(genericToStringSerializer);
        return redisTemplate;
    }
    
    @Bean(name="jsonRedisTemplate")
    public RedisTemplate<String, Object> jsonRedisTemplate(
    		RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    
    /**
     * 缓存管理器.
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager cacheManager(@Qualifier("redisTemplate")RedisTemplate<Serializable,Serializable> redisTemplate) {
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
