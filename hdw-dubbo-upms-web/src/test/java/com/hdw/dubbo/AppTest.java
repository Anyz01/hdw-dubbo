package com.hdw.dubbo;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hdw.upms.entity.User;
import com.hdw.upms.entity.vo.UserVo;
import com.hdw.upms.api.IUpmsApiService;
import com.hdw.upms.api.IUserService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());  

	@Autowired
	private IUserService userService;
	
	@Autowired
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
	    
    @Test  
    public void contextLoads() {  
        logger.trace("I am trace log.");  
        logger.debug("I am debug log.");  
        logger.warn("I am warn log.");  
        logger.error("I am error log.");  
    }  
    
    
    @Value("${sso.cas.server.loginUrl}")
	private String loginUrl;

	@Value("${sso.cas.server.prefixUrl}")
	private String prefixUrl;

	@Value("${sso.cas.client.callbackUrl}")
	private String callbackUrl;

	@Value("${sso.cas.serviceUrl}")
	private String serviceUrl;

	@Value("${jwt.salt}")
	private String salt;
	
	
	 @Test  
	    public void testValue() {  
	       
	        logger.error("loginUrl:"+loginUrl);  
	    }  
	    
	
}
