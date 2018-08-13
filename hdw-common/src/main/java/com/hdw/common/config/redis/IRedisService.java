package com.hdw.common.config.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author TuMinglong
 * @description Cache工具类
 * @date 2017年11月13日下午2:23:26
 */
public interface IRedisService {

    /**
     * 查看key是否存在
     *
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * 删除key
     *
     * @param key
     */
    void del(String key);

    /**
     * 批量删除key
     *
     * @param keys
     */
    void delAll(List<String> keys);

    /**
     * 实现命令：expire设置过期时间，单位秒
     *
     * @param key
     * @param second
     * @return
     */
    boolean expire(String key, int second);

    /**
     * 在某个时间点失效
     *
     * @param key
     * @param unixTime
     * @return
     */
    boolean exireAt(String key, long unixTime);

    /**
     * 实现命令：返回给定key的剩余生存时间（TTL,time to live）
     *
     * @param key
     * @return
     */
    long ttl(String key);

    /**
     * 实现命令：将键的整数值按给定的数值增加
     *
     * @param key
     * @param num
     * @return
     */
    long incr(String key, long num);

    /**
     * 实现命令：KEYS pattern,查找所有符合给定模式pattern的key
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 返回 key 所储存的值的类型
     *
     * @param key
     * @return
     */
    String getType(String key);

    /**
     * 获取java 8种基本类型的数据请直接使用
     *
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 添加
     *
     * @param key
     * @param value
     * @param seconds 失效时间
     */
    void set(String key, Object value, int seconds);

    /**
     * 添加
     *
     * @param key
     * @param value
     */
    void set(String key, Object value);

    /**
     * 添加list
     *
     * @param key
     * @param values
     * @param second 失效时间
     */
    <T> void ladd(String key, T values, int second);

    /**
     * 添加list
     *
     * @param key
     * @param values
     */
    <T> void ladd(String key, T values);

    /**
     * 添加list
     *
     * @param key
     * @param values
     * @param second 失效时间
     */
    <T> void ladd(String key, List<T> values, int second);

    /**
     * 添加list
     *
     * @param key
     * @param values
     */
    <T> void ladd(String key, List<T> values);

    /**
     * 修剪现有列表，使其只包含指定的指定范围的元素，起始和停止都是基于0的索引
     *
     * @param key
     * @param start
     * @param end
     */
    void ltrim(String key, long start, long end);

    /**
     * 返回存储在键中的列表的长度。如果键不存在，则将其解释为空列表，并返回0。当key存储的值不是列表时返回错误。
     *
     * @param key
     * @return
     */
    Long lsize(String key);

    /**
     * 获取list
     *
     * @param key
     * @return
     */
    <T> List<T> lget(String key);

    /**
     * 返回存储在键中的列表的指定元素。偏移开始和停止是基于零的索引，其中0是列表的第一个元素（列表的头部），1是下一个元素 获取list
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    <T> List<T> lget(String key, Long start, Long end);

    /**
     * 添加set
     *
     * @param key
     * @param value
     * @param second 失效时间（秒）
     */
    void sadd(String key, Object value, int second);

    /**
     * 添加时间
     *
     * @param key
     * @param value
     */
    void sadd(String key, Object value);

    /**
     * 获取set
     *
     * @param key
     * @return
     */
    Set<Object> sget(String key);

    /**
     * 删除set其中某项值
     *
     * @param key
     * @param value
     * @return
     */
    boolean sdel(String key, Object value);

    /**
     * 添加map
     *
     * @param key
     * @param par
     * @param second 失效时间(秒)
     */
    void madd(String key, Map<String, Object> par, int second);

    /**
     * 添加map
     *
     * @param key
     * @param par
     */
    void madd(String key, Map<String, Object> par);

    /**
     * 获取map
     *
     * @param key
     * @return
     */
    Map<String, Object> mget(String key);

    /**
     * 获取map中的value
     *
     * @param key
     * @param field map中key
     * @return
     */
    Object mget(String key, String field);

    /**
     * 删除map中某项值
     *
     * @param key
     * @return
     */
    boolean mdel(String key, String field);

}
