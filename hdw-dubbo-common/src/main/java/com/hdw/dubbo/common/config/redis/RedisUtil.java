package com.hdw.dubbo.common.config.redis;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 
 * @description Redis工具类
 * @author TuMinglong
 * @date 2018年1月25日 上午10:48:30
 */
@Component
public class RedisUtil {

	@Autowired
	private RedisTemplate<Serializable, Serializable> redisTemplate;

	@Value("${spring.redis.expiration}")
	private Integer EXPIRE;

	public boolean exists(String key) {

		return redisTemplate.hasKey(key);
	}

	public void del(String key) {
		redisTemplate.delete(key);

	}

	public void delAll(String keys) {
		redisTemplate.delete(redisTemplate.keys(keys));

	}

	public boolean expire(String key, int second) {

		return redisTemplate.expire(key, second, TimeUnit.SECONDS);
	}

	public boolean exireAt(String key, long unixTime) {
		return redisTemplate.expireAt(key, new Date(unixTime));
	}

	public long ttl(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	public Object get(String key) {

		return redisTemplate.opsForValue().get(key);
	}

	public void set(String key, Serializable value, int seconds) {
		redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
	}

	public void set(String key, Serializable value) {
		redisTemplate.opsForValue().set(value, value);
		expire(key, EXPIRE);

	}

	@SuppressWarnings("unchecked")
	public <T> List<T> lget(String key) {
		return (List<T>) redisTemplate.opsForList().leftPop(key);
	}

	public <T> void ladd(String key, List<T> values, int second) {
		redisTemplate.opsForList().leftPush(key, (Serializable) values);
		expire(key, second);
	}

	public <T> void ladd(String key, List<T> values) {
		redisTemplate.opsForList().leftPush(key, (Serializable) values);
		expire(key, EXPIRE);
	}

	public Set<Serializable> sget(String key) {

		return redisTemplate.opsForSet().members(key);
	}

	public void sadd(String key, Serializable value, int second) {
		redisTemplate.opsForSet().add(key, value);
		expire(key, second);

	}

	public void sadd(String key, Serializable value) {
		redisTemplate.opsForSet().add(key, value);
		expire(key, EXPIRE);
	}

	public boolean sdel(String key, Serializable value) {
		Long flag = redisTemplate.opsForSet().remove(key, value);
		if (flag == 1) {
			return true;
		} else {
			return false;
		}

	}

	public Map<String, Object> mget(String key) {
		Map<Object, Object> resultMap = redisTemplate.opsForHash().entries(key);
		Map<String, Object> map = new HashMap<String, Object>();
		for (Object obj : resultMap.keySet()) {
			map.put(obj.toString(), resultMap.get(obj));
		}
		return map;
	}

	public Object mget(String key, String field) {
		Object value = redisTemplate.opsForHash().get(key, field);
		return value;
	}

	public void madd(String key, Map<String, Object> par, int second) {
		redisTemplate.opsForHash().putAll(key, par);
		expire(key, second);

	}

	public void madd(String key, Map<String, Object> par) {
		redisTemplate.opsForHash().putAll(key, par);
		expire(key, EXPIRE);
	}

	public boolean mdel(String key, String field) {
		return redisTemplate.opsForHash().delete(key, field) == 1;
	}

	public String getType(String key) {
		return redisTemplate.type(key).getClass().getName();
	}

}
