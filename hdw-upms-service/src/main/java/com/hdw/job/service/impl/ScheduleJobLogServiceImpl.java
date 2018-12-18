package com.hdw.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.result.PageUtils;
import com.hdw.job.entity.ScheduleJobLogEntity;
import com.hdw.job.mapper.ScheduleJobLogMapper;
import com.hdw.job.service.ScheduleJobLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogMapper, ScheduleJobLogEntity> implements ScheduleJobLogService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String jobId = (String)params.get("jobId");
		Page<ScheduleJobLogEntity> page = new PageUtils<ScheduleJobLogEntity>(params).getPage();
		QueryWrapper<ScheduleJobLogEntity> queryWrapper=new QueryWrapper<ScheduleJobLogEntity>().like(StringUtils.isNotBlank(jobId),"job_id", jobId);
		List<ScheduleJobLogEntity> list =this.page(page,queryWrapper).getRecords();
		page.setRecords(list);
		return new PageUtils<ScheduleJobLogEntity>(page);
	}
}
