package com.hdw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


/**
 * 
 * @description Application
 * @author TuMinglong
 * @date 2017年9月5日下午8:55:08
 *
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class UpmsServerApplication extends SpringBootServletInitializer {

	protected final static Logger logger = LoggerFactory.getLogger(UpmsServerApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		logger.info("----UpmsServerApplication 启动----");
		return application.sources(UpmsServerApplication.class);
		
	}

	public static void main(String[] args) {
		SpringApplication.run(UpmsServerApplication.class, args);
		logger.info("----UpmsServerApplication 启动----");
	}

}
