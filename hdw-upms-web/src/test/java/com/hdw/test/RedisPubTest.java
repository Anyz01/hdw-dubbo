package com.hdw.test;

import com.hdw.common.config.redis.pub.MessageSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description com.hdw.test
 * @Author TuMinglong
 * @Date 2018/5/24 10:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisPubTest {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSender messageSender;

    @Test
    public void sendMessage(){
        for (int i=0;i<100;i++){
            messageSender.sendObjMessage("test","message"+i);
        }
    }
}
