package com.hdw.dubbo.common.config.db;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * 
 * @description  数据源切换切面
 * @author TuMinglong
 * @date 2018年1月24日 下午4:07:35
 */
@Aspect
@Component
public class DataSourceAspect {
    protected static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);


    @Before("@annotation(DynamicDS)")
    public void beforeDynamicDS(JoinPoint point){

        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();

        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        @SuppressWarnings("rawtypes")
		Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
        String dataSource = DataSourceEnum.MASTER.getDefault();
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            // 判断是否存在@DS注解
            if (method.isAnnotationPresent(DynamicDS.class)) {
                DynamicDS annotation = method.getAnnotation(DynamicDS.class);
                // 取出注解中的数据源名
                dataSource = annotation.value();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("切换到数据源{}",dataSource);
        // 切换数据源
        DynamicDataSource.setDataSource(dataSource);

    }

    @After("@annotation(DynamicDS)")
    public void afterDynamicDS(JoinPoint point){
        DynamicDataSource.clearDataSource();
    }
}
