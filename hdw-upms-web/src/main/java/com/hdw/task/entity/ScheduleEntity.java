package com.hdw.task.entity;

import java.io.Serializable;

/**
 * @description 定时任务实体
 * @author TuMinglong
 * @date 2016-12-20
 *
 */
public class ScheduleEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String jobName;//任务名
	private String jobGroup;//任务组
	private String cronExpression;//cron表达式
	private String status;//状态
	private String description;//描述
	private String className;//执任务的类（完整路径 包含包名）
	private String methodName;//执行任务的方法名
	
	public ScheduleEntity(){
		super();
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public String toString() {
		 return "ScheduleEntity{" +
	                "jobName='" + jobName + '\'' +
	                ", jobGroup='" + jobGroup + '\'' +
	                ", cronExpression='" + cronExpression + '\'' +
	                ", status='" + status + '\'' +
	                ", description='" + description + '\'' +
	                ", className='" + className + '\'' +
	                ", methodName='" + methodName + '\'' +
	                '}';
	}
	
	

}
