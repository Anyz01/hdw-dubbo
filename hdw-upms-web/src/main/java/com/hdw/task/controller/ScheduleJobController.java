package com.hdw.task.controller;

import com.hdw.common.base.BaseController;
import com.hdw.common.result.PageInfo;
import com.hdw.task.entity.ScheduleEntity;
import com.hdw.task.service.ScheduleJobService;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TuMinglong
 * @version v1.0
 * @description 定时器配置管理
 * @date 2016-12-20
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleJobController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleJobController.class);

    @Autowired
    private ScheduleJobService scheduleJobService;


    /**
     * 列表页面
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "task/scheduleList";
    }

    @RequestMapping("/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(Integer offset, Integer limit, String sort, String order) {
        List<ScheduleEntity> scheduleEntities = scheduleJobService.getAllScheduleJob();
        PageInfo pageInfo = new PageInfo(offset, limit);
        pageInfo.setRows(scheduleEntities);
        pageInfo.setTotal(scheduleEntities.size());
        return pageInfo;
    }

    /**
     * 添加页面
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "task/scheduleAdd";
    }

    /**
     * 添加
     *
     * @param scheduleEntity
     * @return
     */
    @PostMapping(value = "add")
    @ResponseBody
    public Object jobSave(ScheduleEntity scheduleEntity) throws Exception {
        String cronExpression = scheduleEntity.getCronExpression().trim();

        // 判断表达式
        boolean f = CronExpression.isValidExpression(cronExpression);

        if (!f) {
            return renderError("cron表达式有误，不能被解析！");
        }
        try {
            scheduleEntity.setCronExpression(cronExpression);
            scheduleEntity.setStatus("1");
            scheduleJobService.add(scheduleEntity);
            return renderSuccess("保存成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return renderError("添加任务失败");
        } catch (SchedulerException e) {
            e.printStackTrace();
            if (e.getMessage().contains("because one already exists with this identification")) {
                return renderError("任务组中存在同样的任务名");
            } else {
                return renderError("未知原因,添加任务失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return renderError("系统错误!");
        }
    }

    /**
     * 暂停任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "stop")
    @ResponseBody
    public Object stopJob(String jobName, String jobGroup) throws Exception {
        try {
            scheduleJobService.stopJob(jobName, jobGroup);
            return renderSuccess("暂停成功");
        } catch (Exception e) {
            return renderError("系统错误!");
        }

    }

    /**
     * 删除任务
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public Object deleteJob(String jobName, String jobGroup) throws Exception {
        try {
            scheduleJobService.delJob(jobName, jobGroup);
            return renderSuccess("删除成功");
        } catch (Exception e) {
            return renderError("系统错误!");
        }
    }

    /**
     * 编辑
     *
     * @param jobName
     * @param jobGroup
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "edit")
    @ResponseBody
    public Map<String, ? extends Object> editJob(@RequestParam("jobName") String jobName,
                                                 @RequestParam("jobGroup") String jobGroup, HttpServletRequest req) throws Exception {
        jobName = new String(jobName.getBytes("ISO-8859-1"), "UTF-8");
        jobGroup = new String(jobGroup.getBytes("ISO-8859-1"), "UTF-8");
        logger.info("编辑页面 任务名：" + jobName + " 任务分组：" + jobGroup);
        List<ScheduleEntity> scheduleEntities = scheduleJobService.getAllScheduleJob();
        Map<String, Object> map = new HashMap<String, Object>();
        for (ScheduleEntity scheduleEntity : scheduleEntities) {
            if (jobName.equals(scheduleEntity.getJobName()) && jobGroup.equals(scheduleEntity.getJobGroup())) {
                map.put("jobName", jobName);
                map.put("jobGroup", jobGroup);
                map.put("cronExpression", scheduleEntity.getCronExpression());
                map.put("className", scheduleEntity.getClassName());
                map.put("methodName", scheduleEntity.getMethodName());
                map.put("description", scheduleEntity.getDescription());
            }
        }
        return map;
    }

    /**
     * 修改页面
     *
     * @return
     */
    @GetMapping("/editPage/{jobName}/{jobGroup}")
    public String editPage(@PathVariable("jobName") String jobName, @PathVariable("jobGroup") String jobGroup, Model model) {
        model.addAttribute("jobGroup", jobGroup);
        model.addAttribute("jobName", jobName);
        return "task/scheduleEdit";
    }

    /**
     * 更新
     *
     * @param scheduleEntity
     * @return
     */
    @RequestMapping(value = "update")
    @ResponseBody
    public Object updateJob(ScheduleEntity scheduleEntity) throws Exception {
        logger.info("更新 任务名：" + scheduleEntity.getJobName() + " 任务分组：" + scheduleEntity.getJobGroup() + " 表达式："
                + scheduleEntity.getCronExpression());

        // 验证cron表达式
        boolean f = CronExpression.isValidExpression(scheduleEntity.getCronExpression());
        if (!f) {
            return renderError("cron表达式有误，不能被解析！");
        }
        try {
            scheduleJobService.modifyTrigger(scheduleEntity.getJobName(), scheduleEntity.getJobGroup(),
                    scheduleEntity.getCronExpression());
            return renderSuccess("修改成功");

        } catch (SchedulerException e) {
            e.printStackTrace();
            return renderError("系统错误!");
        }
    }

    /**
     * 立即运行一次
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "startNow")
    @ResponseBody
    public Object stratNow(String jobName, String jobGroup) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", -1);
        try {
            scheduleJobService.startNowJob(jobName, jobGroup);
            return renderSuccess("执行成功");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return renderError("系统错误!");
        }

    }

    /**
     * 恢复
     *
     * @param jobName
     * @param jobGroup
     * @return
     */
    @RequestMapping(value = "resume")
    @ResponseBody
    public Object resumeJob(String jobName, String jobGroup) throws Exception {

        try {
            scheduleJobService.restartJob(jobName, jobGroup);
            return renderSuccess("恢复成功");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return renderError("系统错误!");
        }
    }

    /**
     * 重启所有设备
     *
     * @return
     */
    @PostMapping("/restart")
    @ResponseBody
    public Object restartDevices() {
        try {
            scheduleJobService.resumeAllScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return renderSuccess();
    }

    /**
     * 停止所有设备
     *
     * @return
     */
    @PostMapping("/stopAll")
    @ResponseBody
    public Object stopDevices() {
        try {
            scheduleJobService.stopScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return renderSuccess();
    }

}
