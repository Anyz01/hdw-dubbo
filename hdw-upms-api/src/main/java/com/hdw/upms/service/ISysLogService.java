package com.hdw.upms.service;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.common.result.PageInfo;
import com.hdw.upms.entity.SysLog;


/**
 * 
 * @Descriptin 日志接口实现层
 * @author TuMinglong
 * @Date 2018年5月6日 下午8:35:34
 */
public interface ISysLogService extends IService<SysLog> {

    PageInfo selectDataGrid(PageInfo pageInfo);
    
}