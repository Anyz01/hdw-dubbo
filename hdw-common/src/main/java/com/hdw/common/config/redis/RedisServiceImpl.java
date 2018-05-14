package com.hdw.common.config.redis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisServiceImpl implements IRedisService {
	
	@Resource(name="stringRedisTemplate")
	private RedisTemplate<String, Object> stringRedisTemplate;
	
	@Resource(name="jsonRedisTemplate")
	private RedisTemplate<String, Object> jsonRedisTemplate;
	
	@Value("${spring.redis.expiration}")
	private Integer EXPIRE;

	@Override
	public long ttl(String key) {
		
		return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	@Override
	public boolean expire(String key, int second) {
		
		return stringRedisTemplate.hasKey(key);
	}

	@Override
	public boolean exireAt(String key, long unixTime) {
		
		return stringRedisTemplate.expireAt(key, new Date(unixTime));
	}

	@Override
	public long incr(String key, long delta) {
		
		return stringRedisTemplate.opsForValue().increment(key, delta);
	}

	@Override
	public Set<String> keys(String pattern) {
		
		return stringRedisTemplate.keys(pattern);
	}

	@Override
	public void del(String key) {
		
		stringRedisTemplate.delete(key);
	}

	@Override
	public void delAll(String keys) {
		
		stringRedisTemplate.delete(stringRedisTemplate.keys(keys));
	}

	@Override
	public boolean exists(String key) {
		
		return stringRedisTemplate.hasKey(key);
	}

	@Override
	public String getType(String key) {
	
		return stringRedisTemplate.type(key).getClass().getName();
	}

	@Override
	public Object get(String key) {
		
		return stringRedisTemplate.opsForValue().get(key);
	}

	@Override
	public void set(String key, Object value, int seconds) {
		
		stringRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
	}

	@Override
	public void set(String key, Object value) {
		
		stringRedisTemplate.opsForValue().set(key, value);
		expire(key, EXPIRE);
	}

	@Override
	public <T> void ladd(String key, T values, int second) {
		
		jsonRedisTemplate.opsForList().leftPush(key, values);
		expire(key, second);
	}

	@Override
	public <T> void ladd(String key, T values) {
		
		jsonRedisTemplate.opsForList().leftPush(key, values);
		expire(key, EXPIRE);
	}

	@Override
	public <T> void ladd(String key, List<T> values, int second) {
		
		for (T t : values) {
			this.ladd(key,t,second);
		}
	}

	@Override
	public <T> void ladd(String key, List<T> values) {
		
		for (T t : values) {
			this.ladd(key,t);
		}
	}

	@Override
	public void ltrim(String key, long start, long end) {
		
		jsonRedisTemplate.opsForList().trim(key, start, end);
	}

	@Override
	public Long lsize(String key) {
		
		return jsonRedisTemplate.opsForList().size(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> lget(String key) {
		
		return (List<T>) jsonRedisTemplate.opsForList().range(key,0,-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> lget(String key, Long start, Long end) {
		
		return (List<T>) jsonRedisTemplate.opsForList().range(key,start,end);
	}

	@Override
	public void sadd(String key, Object value, int second) {
		
		jsonRedisTemplate.opsForSet().add(key, value);
		expire(key, second);
	}

	@Override
	public void sadd(String key, Object value) {
		
		jsonRedisTemplate.opsForSet().add(key, value);
		expire(key, EXPIRE);
	}

	@Override
	public Set<Object> sget(String key) {
		
		return jsonRedisTemplate.opsForSet().members(key);
	}

	@Override
	public boolean sdel(String key, Object value) {
		
		long flag=jsonRedisTemplate.opsForSet().remove(key, value);
		if(flag==1) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public void madd(String key, Map<String, Object> par, int second) {
		
		stringRedisTemplate.opsForHash().putAll(key, par);
		expire(key, second);
	}

	@Override
	public void madd(String key, Map<String, Object> par) {
		
		stringRedisTemplate.opsForHash().putAll(key, par);
		expire(key, EXPIRE);
	}

	@Override
	public Map<String, Object> mget(String key) {
		
		Map<Object, Object> resultMap= stringRedisTemplate.opsForHash().entries(key);
		Map<String,Object> map=new HashMap<String,Object>();
		for (Object obj : resultMap.keySet()) {
			map.put(obj.toString(), resultMap.get(obj));
		}
	    return map;
	}

	@Override
	public Object mget(String key, String field) {
		
		 Object value=stringRedisTemplate.opsForHash().get(key,field);  
		 return value; 
	}

	@Override
	public boolean mdel(String key, String field) {
		
		return stringRedisTemplate.opsForHash().delete(key, field)==1;
	}

}
