package com.hdw.upms.scan;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @description Http拦截器
 * @author TuMinglong
 * @date 2017年9月6日上午11:38:06
 *
 */
@Aspect
@Component
public class WebAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebAspect.class);
    
    private long startTime=0;
    private long endTime=0;
    
    @Pointcut("execution(* com.hdw.dubbo.*.controller.*.*(..))")
    public void init(){

    }
    
    @Before("init()")
    public void doBefore(JoinPoint joinPoint){
    	startTime=System.currentTimeMillis();
        //URL
        ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("url={}",request.getRequestURL());
        //method
        logger.info("method={}",request.getMethod());
        //ip
        logger.info("ip={}",request.getRemoteAddr());
        //类方法
        logger.info("class_method={}",joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        //参数
        logger.info("args={}",joinPoint.getArgs());
    }

    @Around(value = "init()")
    public Object doAround(ProceedingJoinPoint proceeding) {
        Object o = null;
        try {
            //执行
            o = proceeding.proceed(proceeding.getArgs());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return o;
    }

    @AfterReturning(returning = "object",pointcut="init()")
     public void doAfterReturning(Object object){
    	if(object!=null) {
    		logger.info("response={}",object.toString());
    	}else {
    		logger.info("response=");
    	}
       
     }

    @After("init()")
    public void doAfter(){
    	endTime=System.currentTimeMillis();
    	long totalSeconds = (endTime-startTime)/1000;
        logger.info("----"+"执行时间："+totalSeconds+"s"+"----");
    }
}
