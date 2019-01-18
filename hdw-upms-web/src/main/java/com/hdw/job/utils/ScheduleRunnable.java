package com.hdw.job.utils;

import com.hdw.common.exception.GlobalException;
import com.hdw.common.util.SpringContextUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;

/**
 * @Description 执行定时任务
 * @Author TuMinglong
 * @Date 2019/1/18 15:59
 **/
public class ScheduleRunnable implements Runnable {
	private Object target;
	private Method method;
	private String params;

	public ScheduleRunnable(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException {
		this.target = SpringContextUtils.getBean(beanName);
		this.params = params;

        if (StringUtils.isNotBlank(params)) {
			this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
			this.method = target.getClass().getDeclaredMethod(methodName);
		}
	}

	@Override
	public void run() {
		try {
			ReflectionUtils.makeAccessible(method);
            if (StringUtils.isNotBlank(params)) {
				method.invoke(target, params);
            } else {
				method.invoke(target);
			}
        } catch (Exception e) {
			throw new GlobalException("执行定时任务失败", e);
		}
	}

}
