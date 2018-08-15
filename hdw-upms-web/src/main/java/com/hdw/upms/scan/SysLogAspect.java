package com.hdw.upms.scan;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.upms.entity.SysLog;
import com.hdw.upms.service.IUpmsApiService;
import com.hdw.upms.shiro.ShiroKit;
import com.hdw.upms.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;


/**
 * @author TuMinglong
 * @description AOP 日志
 * @date 2018年3月6日 上午10:14:28
 */
@Aspect
@Component
public class SysLogAspect {
    private static final Logger LOGGER = LogManager.getLogger(SysLogAspect.class);
    private long startTime = 0;
    private long endTime = 0;
    private long beginTime = 0;


    @Reference(application = "${dubbo.application.id}", group = "hdw-upms")
    private IUpmsApiService upmsApiService;

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void cutController() {
    }

    @Before("cutController()")
    public void doBefore(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();
        beginTime = System.currentTimeMillis();
        //URL
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        LOGGER.info("url={}", request.getRequestURL());
        //method
        LOGGER.info("method={}", request.getMethod());
        //ip
        LOGGER.info("ip={}", request.getRemoteAddr());
        //类方法
        LOGGER.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //参数
        LOGGER.info("args={}", joinPoint.getArgs());
    }

    @Around("cutController()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        String strMethodName = point.getSignature().getName();
        String strClassName = point.getTarget().getClass().getName();
        Object[] params = point.getArgs();
        StringBuffer bfParams = new StringBuffer();
        Enumeration<String> paraNames = null;
        HttpServletRequest request = null;
        if (params != null && params.length > 0) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            paraNames = request.getParameterNames();
            String key;
            String value;
            while (paraNames.hasMoreElements()) {
                key = paraNames.nextElement();
                value = request.getParameter(key);
                bfParams.append(key).append("=").append(value).append("&");
            }
            if (StringUtils.isBlank(bfParams)) {
                bfParams.append(request.getQueryString());
            }
        }
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        String strMessage = String.format("[类名]:%s,[方法]:%s,[参数]:%s", strClassName, strMethodName, bfParams.toString());
        LOGGER.info(strMessage);
        if (isWriteLog(strMethodName)) {
            try {
                ShiroUser shiroUser = ShiroKit.getUser();
                if (null != shiroUser) {
                    String loginName = shiroUser.getLoginName();
                    SysLog sysLog = new SysLog();
                    sysLog.setLoginName(loginName);
                    sysLog.setRoleName(shiroUser.getRoles().get(0));
                    sysLog.setClassName(strClassName);
                    sysLog.setMethod(strMethodName);
                    if (StringUtils.isNotBlank(bfParams.toString())) {
                        sysLog.setParams(bfParams.toString());
                    }
                    sysLog.setCreateTime(new Date());
                    if (request != null) {
                        sysLog.setClientIp(request.getRemoteAddr());
                    }
                    sysLog.setTime(time);
                    LOGGER.info(sysLog.toString());
                    upmsApiService.insertSysLog(sysLog);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        return point.proceed();
    }

    @AfterReturning(returning = "object", pointcut = "cutController()")
    public void doAfterReturning(Object object) {
        if (object != null) {
            LOGGER.info("response={}", object.toString());
        } else {
            LOGGER.info("response=");
        }

    }

    @After("cutController()")
    public void doAfter() {
        endTime = System.currentTimeMillis();
        long totalMillis = endTime - startTime;
        LOGGER.info("----" + "执行时间：" + totalMillis + "毫秒" + "----");
    }

    private boolean isWriteLog(String method) {
        String[] pattern = {"login", "logout", "add", "edit", "delete", "grant"};
        for (String s : pattern) {
            if (method.indexOf(s) > -1) {
                return true;
            }
        }
        return false;
    }
}
