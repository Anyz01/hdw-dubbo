package com.hdw;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import de.codecentric.boot.admin.config.EnableAdminServer;

/**
 * 
 * @description Application
 * @author TuMinglong
 * @date 2017年9月5日下午8:55:08
 *
 */
@Configuration
@EnableAutoConfiguration
@EnableAdminServer
public class MonitorApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MonitorApplication.class);
		
	}

	public static void main(String[] args) {
		SpringApplication.run(MonitorApplication.class, args);
	}

}
