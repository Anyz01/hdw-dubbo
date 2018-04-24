package com.hdw.common.config.db;

import java.lang.annotation.*;


/**
 * 
 * @description 自定义注解
 * @author TuMinglong
 * @date 2018年1月24日 下午4:08:53
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DynamicDS {
    String value() default "masterDataSource";
}
