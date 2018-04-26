package com.hdw.dubbo;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hdw.upms.entity.User;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.service.IUpmsApiService;
import com.hdw.upms.service.IUserService;
import com.alibaba.dubbo.config.annotation.Reference;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());  

	@Reference(version = "1.0.0", application = "${dubbo.application.id}", url = "dubbo://localhost:20880")
	private IUserService userService;

	@Reference(version = "1.0.0", application = "${dubbo.application.id}", url = "dubbo://localhost:20880")
	private IUpmsApiService upmsApiService;
	
	

	@Test
	public void testDobboByUserId(){
		UserVo user=userService.selectVoById(1l);
		System.out.println("dubbo消息："+user.getLoginName());
	}
	
	@Test
	public void testDobboByUser(){
		UserVo uservo = new UserVo();
		uservo.setLoginName("admin");
		List<User> list = upmsApiService.selectByLoginName(uservo);
		if(list!=null && !list.isEmpty()) {
			User user = list.get(0);
			System.out.println("dubbo消息："+user.getName());
		}
					
	}
	    
}
