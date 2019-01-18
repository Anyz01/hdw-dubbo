package com.hdw.job.controller;

import com.hdw.common.result.PageUtils;
import com.hdw.common.result.ResultMap;
import com.hdw.common.validator.ValidatorUtils;
import com.hdw.job.entity.ScheduleJobEntity;
import com.hdw.job.service.IScheduleJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * @Description 定时任务
 * @Author TuMinglong
 * @Date 2019/1/18 15:59
 **/
@RestController
@RequestMapping("/sys/schedule")
public class ScheduleJobController {
	@Autowired
    private IScheduleJobService scheduleJobService;

	/**
	 * 定时任务列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys/schedule/list")
    public ResultMap list(@RequestParam Map<String, Object> params) {
		PageUtils page = scheduleJobService.queryPage(params);

		return ResultMap.ok().put("page", page);
	}

	/**
	 * 定时任务信息
	 */
	@GetMapping("/info/{jobId}")
	@RequiresPermissions("sys/schedule/info")
    public ResultMap info(@PathVariable("jobId") Long jobId) {
		ScheduleJobEntity schedule = scheduleJobService.getById(jobId);

		return ResultMap.ok().put("schedule", schedule);
	}

	/**
	 * 保存定时任务
	 */
	@PostMapping("/save")
	@RequiresPermissions("sys/schedule/save")
    public ResultMap save(@RequestBody ScheduleJobEntity scheduleJob) {
		ValidatorUtils.validateEntity(scheduleJob);

        scheduleJobService.insert(scheduleJob);

		return ResultMap.ok();
	}

	/**
	 * 修改定时任务
	 */
	@PostMapping("/update")
	@RequiresPermissions("sys/schedule/update")
    public ResultMap update(@RequestBody ScheduleJobEntity scheduleJob) {
		ValidatorUtils.validateEntity(scheduleJob);

        scheduleJobService.update(scheduleJob);

        return ResultMap.ok();
	}

    /**
	 * 删除定时任务
	 */
	@PostMapping("/delete")
	@RequiresPermissions("sys/schedule/delete")
    public ResultMap delete(@RequestBody Long[] jobIds) {
		scheduleJobService.deleteBatch(jobIds);

        return ResultMap.ok();
	}

    /**
	 * 立即执行任务
	 */
	@PostMapping("/run")
	@RequiresPermissions("sys/schedule/run")
    public ResultMap run(@RequestBody Long[] jobIds) {
		scheduleJobService.run(jobIds);

        return ResultMap.ok();
	}

    /**
	 * 暂停定时任务
	 */
	@PostMapping("/pause")
	@RequiresPermissions("sys/schedule/pause")
    public ResultMap pause(@RequestBody Long[] jobIds) {
		scheduleJobService.pause(jobIds);

        return ResultMap.ok();
	}

    /**
	 * 恢复定时任务
	 */
	@PostMapping("/resume")
	@RequiresPermissions("sys/schedule/resume")
    public ResultMap resume(@RequestBody Long[] jobIds) {
		scheduleJobService.resume(jobIds);

        return ResultMap.ok();
	}

}
