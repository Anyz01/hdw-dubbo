package com.hdw.test;


import com.hdw.mq.config.ActiveMQSendMsgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description activemq测试类
 * @Author TuMinglong
 * @Date 2018/5/24 10:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActiveMQTest {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActiveMQSendMsgService activeMQSendMsgService;

    /**
     * 生产者/消费者模式
     */
    @Test
    public void sendMessageofQueue(){
        for (int i=0;i<100;i++){
            activeMQSendMsgService.sendObjectMessage("test","message"+i);
        }
    }

    /**
     * 发布/订阅模式
     */
    @Test
    public void sendMessageOfTopic(){
        for (int i=0;i<100;i++){
            activeMQSendMsgService.sendObjectMessageOfTopic("testTopic","message"+i);
        }
    }
}
