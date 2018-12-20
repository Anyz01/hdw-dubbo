package com.hdw.job.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.upms.entity.SysUser;
import com.hdw.upms.service.ISysUserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @Description 测试定时任务
 * testTask为spring bean的名称
 * @Author TuMinglong
 * @Date 2018/12/13 10:44
 */
@Component("testTask")
public class TestTask {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Reference
	private ISysUserService sysUserService;
	
	public void test(String params){
		logger.info("我是带参数的test方法，正在被执行，参数为：" + params);
		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SysUser user = sysUserService.getById(1L);
		System.out.println(ToStringBuilder.reflectionToString(user));
		
	}
	public void test2(){
		logger.info("我是不带参数的test2方法，正在被执行");
	}
}
