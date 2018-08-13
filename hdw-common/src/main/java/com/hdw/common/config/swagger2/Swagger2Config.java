package com.hdw.common.config.swagger2;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Sets;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author TuMinglong
 * @description Swagger2配置
 * @date 2018年3月6日 上午10:42:11
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .produces(Sets.newHashSet("application/json"))
                .consumes(Sets.newHashSet("application/json"))
                .protocols(Sets.newHashSet("http", "https"))
                .apiInfo(apiInfo())
                .forCodeGeneration(true)
                .useDefaultResponseMessages(false).select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 指定controller存放的目录路径
                //.apis(RequestHandlerSelectors.basePackage("com.hdw.upms.controller"))
                // .paths(PathSelectors.ant("/api/v1/*"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title("华德威系统API服务")
                // 文档描述
                .description("华德威系统API接口文档简要描述")
                .version("v1")
                .license("MIT 协议")
                .licenseUrl("http://www.opensource.org/licenses/MIT")
                .contact(new Contact("涂明龙", "https://github.com/tumao2/hdw-dubbo", "tuminglong@126.com")).build();
    }

}
