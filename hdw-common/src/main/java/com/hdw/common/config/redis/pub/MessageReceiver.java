package com.hdw.common.config.redis.pub;

import org.springframework.stereotype.Component;

/**
 * @Description Redis 发布订阅模式 消息接收者
 * @Author TuMinglong
 * @Date 2018/5/24 10:17
 */
@Component
public class MessageReceiver {
    /**
     * 接收消息
     * @param obj
     */
    public void receiveMessage(Object obj){
        System.out.println("==== Redis 发布订阅模式 收到的消息："+obj.toString()+" ====");
    }
}
