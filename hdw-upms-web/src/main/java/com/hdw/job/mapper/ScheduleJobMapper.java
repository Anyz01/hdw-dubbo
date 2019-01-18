package com.hdw.job.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hdw.job.entity.ScheduleJobEntity;

import java.util.Map;

/**
 * @Description 定时任务
 * @Author TuMinglong
 * @Date 2018/12/13 10:44
 */
public interface ScheduleJobMapper extends BaseMapper<ScheduleJobEntity> {

    /**
     * 批量更新状态
     */
    int updateBatch(Map<String, Object> map);
}
