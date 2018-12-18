package com.hdw.job.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.result.PageUtils;
import com.hdw.job.entity.ScheduleJobLogEntity;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author Mark tuminglong@126.com
 * @since 1.2.0 2016-11-28
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
}
