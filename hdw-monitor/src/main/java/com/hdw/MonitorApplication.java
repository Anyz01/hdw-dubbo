package com.hdw;




import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * 
 * @description Application
 * @author TuMinglong
 * @date 2017年9月5日下午8:55:08
 *
 */

@SpringBootApplication
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
