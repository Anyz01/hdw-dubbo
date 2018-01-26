package com.hdw.dubbo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

/**
 * 
 * @description Application
 * @author TuMinglong
 * @date 2017年9月5日下午8:55:08
 *
 */
@SpringBootApplication
@ImportResource(locations={"classpath:dubbo-provider.xml"})
public class UpmsServiceApplication extends SpringBootServletInitializer {

	protected final static Logger logger = LoggerFactory.getLogger(UpmsServiceApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		logger.info("----UpmsServiceApplication 启动----");
		return application.sources(UpmsServiceApplication.class);
		
	}

	public static void main(String[] args) {
		SpringApplication.run(UpmsServiceApplication.class, args);
		logger.info("----UpmsServiceApplication 启动----");
	}

}
