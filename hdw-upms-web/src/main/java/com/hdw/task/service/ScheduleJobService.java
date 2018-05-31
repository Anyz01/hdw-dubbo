package com.hdw.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdw.task.entity.ScheduleEntity;
import com.hdw.task.job.ScheduledJob;



/**
 * @description ScheduleJob 接口
 * @author TuMinglong
 * @date 2018-1-11
 * @version v2.0
 *
 */
@Service
public class ScheduleJobService {
	@Autowired
	private Scheduler scheduler;

	/**
	 * 添加定时任务
	 * 
	 * @param scheduleEntity
	 * @throws ClassNotFoundException
	 * @throws SchedulerException
	 */
	public void add(ScheduleEntity scheduleEntity) throws ClassNotFoundException, SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class)
				.withIdentity(scheduleEntity.getJobName(), scheduleEntity.getJobGroup())
				.usingJobData("className", scheduleEntity.getClassName())
				.usingJobData("methodName", scheduleEntity.getMethodName()).build();
		jobDetail.getJobDataMap().put("scheduleEntity", scheduleEntity);
		// 表达式调度构建器（可判断创建SimpleScheduleBuilder）
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleEntity.getCronExpression());
		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(scheduleEntity.getJobName(), scheduleEntity.getJobGroup()).withSchedule(scheduleBuilder)
				.build();
		scheduler.scheduleJob(jobDetail, trigger);

	}

	/**
	 * 获取所有JobDetail
	 * 
	 * @return 结果集合
	 */
	public List<JobDetail> getJobs() {
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			List<JobDetail> jobDetails = new ArrayList<JobDetail>();
			for (JobKey key : jobKeys) {
				jobDetails.add(scheduler.getJobDetail(key));
			}
			return jobDetails;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取所有计划中的任务
	 * 
	 * @return 结果集合
	 */
	public List<ScheduleEntity> getAllScheduleJob() {
		List<ScheduleEntity> scheduleEntityList = new ArrayList<ScheduleEntity>();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		try {
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			for (JobKey jobKey : jobKeys) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					ScheduleEntity scheduleEntity = new ScheduleEntity();
					scheduleEntity.setJobName(jobKey.getName());
					scheduleEntity.setJobGroup(jobKey.getGroup());
					Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
					scheduleEntity.setStatus(triggerState.name());
					// 获取要执行的定时任务类名
					JobDetail jobDetail = scheduler.getJobDetail(jobKey);
					// 获取class及method
					JobDataMap dataMap = jobDetail.getJobDataMap();
					scheduleEntity.setClassName(dataMap.getString("className"));
					scheduleEntity.setMethodName(dataMap.getString("methodName"));
					// 判断trigger
					if (trigger instanceof SimpleTrigger) {
						SimpleTrigger simple = (SimpleTrigger) trigger;
						scheduleEntity.setCronExpression(
								"重复次数：" + (simple.getRepeatCount() == -1 ? "无限" : simple.getRepeatCount()) + ",重复间隔:"
										+ (simple.getRepeatInterval() / 1000L));
						scheduleEntity.setDescription(simple.getDescription());
					}
					if (trigger instanceof CronTrigger) {
						CronTrigger cron = (CronTrigger) trigger;
						scheduleEntity.setCronExpression(cron.getCronExpression());
						scheduleEntity.setDescription(
								cron.getDescription() == null ? ("触发器:" + trigger.getKey()) : cron.getDescription());
					}
					scheduleEntityList.add(scheduleEntity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scheduleEntityList;
	}

	/**
	 * 获取所有运行中的任务
	 * 
	 * @return 结果集合
	 */
	public List<ScheduleEntity> getAllRunScheduleJob() {
		List<ScheduleEntity> scheduleEntityList = null;
		try {
			List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
			scheduleEntityList = new ArrayList<ScheduleEntity>(executingJobs.size());
			for (JobExecutionContext executingJob : executingJobs) {
				ScheduleEntity scheduleEntity = new ScheduleEntity();
				JobDetail jobDetail = executingJob.getJobDetail();
				JobKey jobKey = jobDetail.getKey();
				Trigger trigger = executingJob.getTrigger();
				scheduleEntity.setJobName(jobKey.getName());
				scheduleEntity.setJobGroup(jobKey.getGroup());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				scheduleEntity.setStatus(triggerState.name());
				// 获取要执行的定时任务类名
				scheduleEntity.setClassName(jobDetail.getJobClass().getName());
				// 判断trigger
				if (trigger instanceof SimpleTrigger) {
					SimpleTrigger simple = (SimpleTrigger) trigger;
					scheduleEntity.setCronExpression(
							"重复次数:" + (simple.getRepeatCount() == -1 ? "无限" : simple.getRepeatCount()) + ",重复间隔:"
									+ (simple.getRepeatInterval() / 1000L));
					scheduleEntity.setDescription(simple.getDescription());
				}
				if (trigger instanceof CronTrigger) {
					CronTrigger cron = (CronTrigger) trigger;
					scheduleEntity.setCronExpression(cron.getCronExpression());
					scheduleEntity.setDescription(cron.getDescription());
				}
				scheduleEntityList.add(scheduleEntity);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return scheduleEntityList;
	}

	/**
	 * 获取所有的触发器
	 * 
	 * @return 结果集合
	 */
	public List<ScheduleEntity> getTriggersInfo() {
		try {
			GroupMatcher<TriggerKey> matcher = GroupMatcher.anyTriggerGroup();
			Set<TriggerKey> Keys = scheduler.getTriggerKeys(matcher);
			List<ScheduleEntity> triggers = new ArrayList<ScheduleEntity>();

			for (TriggerKey key : Keys) {
				Trigger trigger = scheduler.getTrigger(key);
				ScheduleEntity scheduleEntity = new ScheduleEntity();
				scheduleEntity.setJobName(trigger.getJobKey().getName());
				scheduleEntity.setJobGroup(trigger.getJobKey().getGroup());
				scheduleEntity.setStatus(scheduler.getTriggerState(key) + "");
				if (trigger instanceof SimpleTrigger) {
					SimpleTrigger simple = (SimpleTrigger) trigger;
					scheduleEntity.setCronExpression(
							"重复次数:" + (simple.getRepeatCount() == -1 ? "无限" : simple.getRepeatCount()) + ",重复间隔:"
									+ (simple.getRepeatInterval() / 1000L));
					scheduleEntity.setDescription(simple.getDescription());
				}
				if (trigger instanceof CronTrigger) {
					CronTrigger cron = (CronTrigger) trigger;
					scheduleEntity.setCronExpression(cron.getCronExpression());
					scheduleEntity.setDescription(cron.getDescription());
				}
				triggers.add(scheduleEntity);
			}
			return triggers;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 暂停任务
	 * 
	 * @param name
	 *            任务名
	 * @param group
	 *            任务组
	 * @throws SchedulerException
	 */
	public void stopJob(String name, String group) throws SchedulerException {
		JobKey key = new JobKey(name, group);
		scheduler.pauseJob(key);
	}

	/**
	 * 恢复任务
	 *
	 * @param name
	 *            任务名
	 * @param group
	 *            任务组
	 */
	public void restartJob(String name, String group) throws SchedulerException {
		JobKey key = new JobKey(name, group);
		scheduler.resumeJob(key);
	}

	/**
	 * 立马执行一次任务
	 *
	 * @param name
	 *            任务名
	 * @param group
	 *            任务组
	 */
	public void startNowJob(String name, String group) throws SchedulerException {
		JobKey jobKey = JobKey.jobKey(name, group);
		scheduler.triggerJob(jobKey);
	}

	/**
	 * 删除任务
	 *
	 * @param name
	 *            任务名
	 * @param group
	 *            任务组
	 */
	public void delJob(String name, String group) throws SchedulerException {
		JobKey key = new JobKey(name, group);
		scheduler.deleteJob(key);
	}

	/**
	 * 修改触发器时间
	 *
	 * @param name
	 *            任务名
	 * @param group
	 *            任务组
	 * @param cron
	 *            cron表达式
	 */
	public void modifyTrigger(String name, String group, String cron) throws SchedulerException {
		TriggerKey key = TriggerKey.triggerKey(name, group);
		CronTrigger newTrigger = (CronTrigger) TriggerBuilder.newTrigger().withIdentity(key)
				.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
		scheduler.rescheduleJob(key, newTrigger);
	}

	/**
	 * 暂停调度器
	 */
	public void stopScheduler() throws SchedulerException {
		if (!scheduler.isInStandbyMode()) {
			scheduler.standby();
		}
	}

	/**
	 * 定时任务调用测试
	 */
	void scheduTest() {
		// System.out.println("定时任务方法调用的测试,调用时间:"+new Date());
	}

	/**
	 * 恢复调度器
	 * 
	 * @throws SchedulerException
	 */
	public void resumeAllScheduler() throws SchedulerException {
		scheduler.resumeAll();
		scheduler.start();
	}

}
