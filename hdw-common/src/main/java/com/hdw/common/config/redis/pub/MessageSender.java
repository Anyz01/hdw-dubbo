package com.hdw.common.config.redis.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description Redis 消息发布/订阅模式 消息发送者
 * @Author TuMinglong
 * @Date 2018/5/24 10:04
 */
@Component
public class MessageSender {
    protected static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 发布消息
     *
     * @param channel 通道名称
     * @param message 内容
     */
    public void sendObjMessage(String channel, Object message) {
        logger.info("==== Redis 发布订阅模式 发送的消息：" + message.toString() + "====");
        redisTemplate.convertAndSend(channel, message);
    }
}
