package com.hdw.dubbo.common.csrf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * @description Csrf过滤注解
 * @author TuMinglong
 * @date 2017年10月2日 下午3:48:24
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsrfToken {
	boolean create() default false;
	boolean remove() default false;
}
