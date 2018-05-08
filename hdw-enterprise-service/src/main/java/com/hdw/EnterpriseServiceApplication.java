package com.hdw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


/**
 * 
 * @description Application
 * @author TuMinglong
 * @date 2017年9月5日下午8:55:08
 *
 */
@SpringBootApplication
public class EnterpriseServiceApplication extends SpringBootServletInitializer {

	protected final static Logger logger = LoggerFactory.getLogger(EnterpriseServiceApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		logger.info("----EnterpriseServiceApplication 启动----");
		return application.sources(EnterpriseServiceApplication.class);
		
	}

	public static void main(String[] args) {
		SpringApplication.run(EnterpriseServiceApplication.class, args);
		logger.info("----EnterpriseServiceApplication 启动----");
	}

}
