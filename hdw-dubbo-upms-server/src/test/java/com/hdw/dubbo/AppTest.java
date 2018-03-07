package com.hdw.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hdw.dubbo.upms.entity.vo.UserVo;
import com.hdw.dubbo.upms.rpc.api.IUserService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());  

	@Autowired
	private IUserService userService;

	@Test
	public void testDobboByUser(){
		UserVo user=userService.selectVoById(1l);
		System.out.println("dubbo消息："+user.getLoginName());
	}
	    
    @Test  
    public void contextLoads() {  
        logger.trace("I am trace log.");  
        logger.debug("I am debug log.");  
        logger.warn("I am warn log.");  
        logger.error("I am error log.");  
    }  
	
}
