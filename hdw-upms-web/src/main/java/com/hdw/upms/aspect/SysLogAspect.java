package com.hdw.upms.aspect;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.upms.entity.SysLog;
import com.hdw.upms.service.ISysLogService;
import com.hdw.upms.service.ISysUserService;
import com.hdw.upms.shiro.ShiroKit;
import com.hdw.upms.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);
    private long startTime = 0;
    private long endTime = 0;
    private String strMethodName ="";
    private String strClassName = "";
    private String args ="";
    private String clientIp="127.0.0.1";

    @Reference
    private ISysUserService sysUserService;

    @Reference
    private ISysLogService sysLogService;

    //@Pointcut("within(@org.springframework.stereotype.Controller *)")
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void cutController() {
    }

    @Before("cutController()")
    public void doBefore(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //ip
        logger.info("ip={}", request.getRemoteAddr());
        //URL
        logger.info("url={}", request.getRequestURL());
        //method
        logger.info("method={}", request.getMethod());
        //类方法
        logger.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //参数
        logger.info("args={}", joinPoint.getArgs());
    }

    @Around("cutController()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        strMethodName = point.getSignature().getName();
        strClassName = point.getTarget().getClass().getName();
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
            args=bfParams.toString();
            clientIp=request.getRemoteAddr();
        }

//        String strMessage = String.format("[类名]:%s,[方法]:%s,[参数]:%s", strClassName, strMethodName, bfParams.toString());
//        logger.info(strMessage);

        return point.proceed();
    }

    @AfterReturning(returning = "object", pointcut = "cutController()")
    public void doAfterReturning(Object object) {
        if (object != null) {
            logger.info("response={}", object.toString());
        } else {
            logger.info("response=");
        }

    }

    @After("cutController()")
    public void doAfter() {
        endTime = System.currentTimeMillis();
        long totalMillis = endTime - startTime;
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
                    if (StringUtils.isNotBlank(args)&&!args.equals("null")) {
                        sysLog.setParams(args);
                    }
                    sysLog.setTime(totalMillis);
                    sysLog.setCreateTime(new Date());
                    sysLog.setClientIp(clientIp);
                    sysLogService.save(sysLog);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        logger.info("----" + "执行时间：" + totalMillis + "毫秒" + "----");
    }

    private boolean isWriteLog(String method) {
        String[] pattern = {"login", "logout", "save", "update", "delete","list","dataGrid","edit","grant"};
        for (String s : pattern) {
            if (method.indexOf(s) > -1) {
                return true;
            }
        }
        return false;
    }
}
