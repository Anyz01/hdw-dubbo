package com.hdw.common.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description redis工具类实现类
 * @Author TuMinglong
 * @Date 2018/5/16 15:09
 */
@Component
public class RedisServiceImpl implements IRedisService {

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "jsonRedisTemplate")
    private RedisTemplate<String, Object> jsonRedisTemplate;

    @Value("${spring.redis.expiration}")
    private Integer EXPIRE;

    @Override
    public boolean exists(String key) {

        return redisTemplate.hasKey(key);
    }

    @Override
    public void del(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delAll(List<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public boolean expire(String key, int second) {
        return redisTemplate.expire(key, second, TimeUnit.SECONDS);
    }

    @Override
    public boolean exireAt(String key, long unixTime) {
        return redisTemplate.expireAt(key, new Date(unixTime));
    }

    @Override
    public long ttl(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public long incr(String key, long num) {
        return redisTemplate.opsForValue().increment(key, num);
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public String getType(String key) {
        return redisTemplate.type(key).getClass().getName();
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, Object value, int seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value) {
        this.set(key, value, EXPIRE);
    }

    @Override
    public <T> void ladd(String key, T values, int second) {
        jsonRedisTemplate.opsForList().leftPush(key, values);
        this.expire(key, second);
    }

    @Override
    public <T> void ladd(String key, T values) {
        this.ladd(key, values, EXPIRE);
    }

    @Override
    public <T> void ladd(String key, List<T> values, int second) {
        for (T t : values) {
            this.ladd(key, t, second);
        }
    }

    @Override
    public <T> void ladd(String key, List<T> values) {
        for (T t : values) {
            this.ladd(key, t, EXPIRE);
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

    @Override
    public <T> List<T> lget(String key) {
        return this.lget(key, 0l, -1l);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> lget(String key, Long start, Long end) {
        return (List<T>) jsonRedisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public void sadd(String key, Object value, int second) {
        jsonRedisTemplate.opsForSet().add(key, value);
        this.expire(key, second);
    }

    @Override
    public void sadd(String key, Object value) {
        this.sadd(key, value, EXPIRE);
    }

    @Override
    public Set<Object> sget(String key) {
        return jsonRedisTemplate.opsForSet().members(key);
    }

    @Override
    public boolean sdel(String key, Object value) {
        long flag = jsonRedisTemplate.opsForSet().remove(key, value);
        if (flag == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void madd(String key, Map<String, Object> par, int second) {
        redisTemplate.opsForHash().putAll(key, par);
        this.expire(key, second);
    }

    @Override
    public void madd(String key, Map<String, Object> par) {
        this.madd(key, par, EXPIRE);
    }

    @Override
    public Map<String, Object> mget(String key) {
        Map<Object, Object> resultMap = redisTemplate.opsForHash().entries(key);
        Map<String, Object> map = new HashMap<String, Object>();
        for (Object obj : resultMap.keySet()) {
            map.put(obj.toString(), resultMap.get(obj));
        }
        return map;
    }

    @Override
    public Object mget(String key, String field) {
        Object value = redisTemplate.opsForHash().get(key, field);
        return value;
    }

    @Override
    public boolean mdel(String key, String field) {
        return redisTemplate.opsForHash().delete(key, field) == 1;
    }
}
