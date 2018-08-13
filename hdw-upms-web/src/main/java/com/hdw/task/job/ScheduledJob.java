package com.hdw.task.job;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdw.common.util.SpringUtils;


/**
 * @author TuMinglong
 * @version v1.0
 * @description ScheduledJob
 * @date 2016-12-20
 */
@DisallowConcurrentExecution
public class ScheduledJob implements Job {
    protected static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        //doSomething
        String className = dataMap.getString("className");
        String methodName = dataMap.getString("methodName");
        logger.info("----执行任务的class:" + className + " ,方法为:" + methodName + "----");
        try {
            if (StringUtils.isNotBlank(className) && StringUtils.isNotBlank(methodName)) {
                Object clazz = SpringUtils.getBean(Class.forName(className));
                Method method = clazz.getClass().getDeclaredMethod(methodName, new Class[]{});
                method.setAccessible(true);
                method.invoke(clazz);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
