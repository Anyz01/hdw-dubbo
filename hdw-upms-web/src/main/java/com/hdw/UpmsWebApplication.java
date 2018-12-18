package com.hdw;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


/**
 * @author TuMinglong
 * @description Application
 * @date 2017年9月5日下午8:55:08
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@EnableJms
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class UpmsWebApplication extends SpringBootServletInitializer {

    protected final static Logger logger = LoggerFactory.getLogger(UpmsWebApplication.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        logger.info("----UpmsWebApplication 启动----");
        return application.sources(UpmsWebApplication.class);

    }

    public static void main(String[] args) {
        SpringApplication.run(UpmsWebApplication.class, args);
        logger.info("----UpmsWebApplication 启动----");
    }

}
