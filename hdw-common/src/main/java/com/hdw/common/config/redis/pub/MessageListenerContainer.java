package com.hdw.common.config.redis.pub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @Description Redis 发布订阅模式 监听器容器配置
 * @Author TuMinglong
 * @Date 2018/5/24 10:11
 */
@Configuration
public class MessageListenerContainer {

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，
     * 该消息监听器通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * @param redisConnectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter listenerAdapter){
        RedisMessageListenerContainer container=new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        //订阅一个叫test的管道
        container.addMessageListener(listenerAdapter,new PatternTopic("test"));
        //这个container可以添加多个messageListener
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器
     * @param receiver
     * @return
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(MessageReceiver receiver){
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        return  new MessageListenerAdapter(receiver,"receiveMessage");
    }

}
