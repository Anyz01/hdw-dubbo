package com.hdw.common.config.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hdw.common.csrf.CsrfInterceptor;
import com.hdw.common.interceptor.FileUploadTypeInterceptor;



/**
 * 
 * @description WEB 初始化相关配置
 * @author TuMinglong
 * @date 2018年1月24日 下午4:22:28
 */
@ControllerAdvice
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Autowired
	private CsrfInterceptor csrfInterceptor;
	
	@Autowired
	private FileUploadTypeInterceptor fileUploadTypeInterceptor;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectMapper objectMapper = builder.build();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);// 忽略 transient 修饰的属性
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        super.configureMessageConverters(converters);
    }

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//注册自定义拦截器，添加拦截路径和排除拦截路径    
		//添加csrf拦截器
        registry.addInterceptor(csrfInterceptor).addPathPatterns("/login");  
        //添加文件上传类型拦截器
        registry.addInterceptor(fileUploadTypeInterceptor).addPathPatterns("/**");   
        
		super.addInterceptors(registry);
	}
    
    
}
