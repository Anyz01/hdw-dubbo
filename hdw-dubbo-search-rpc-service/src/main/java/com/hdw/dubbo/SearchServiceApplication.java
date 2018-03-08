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
 * @description SearchServiceApplication
 * @author TuMinglong
 * @date 2017年9月5日下午8:55:08
 *
 */
@SpringBootApplication
@ImportResource(locations={"classpath:dubbo-provider.xml"})
public class SearchServiceApplication extends SpringBootServletInitializer {

	protected final static Logger logger = LoggerFactory.getLogger(SearchServiceApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		logger.info("----SearchServiceApplication 启动----");
		return application.sources(SearchServiceApplication.class);
		
	}

	public static void main(String[] args) {
		SpringApplication.run(SearchServiceApplication.class, args);
		logger.info("----SearchServiceApplication 启动----");
	}

}
