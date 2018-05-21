package com.hdw;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.service.IUpmsApiService;
import com.hdw.upms.service.IUserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());  

	@Reference(version = "1.0.0", application = "${dubbo.application.id}")
	private IUserService userService;

	@Reference(version = "1.0.0", application = "${dubbo.application.id}")
	private IUpmsApiService upmsApiService;
	
	@Test
	public void testDobboByUserId(){
		UserVo user=userService.selectVoById(1l);
		System.out.println("dubbo消息："+user.getLoginName());
	}
	
	@Test
	public void testDobboByUser(){
		UserVo userVo = upmsApiService.selectByLoginName("user");
		if(userVo!=null) {
			System.out.println("dubbo消息："+userVo.getName());
		}
					
	}
	
}
