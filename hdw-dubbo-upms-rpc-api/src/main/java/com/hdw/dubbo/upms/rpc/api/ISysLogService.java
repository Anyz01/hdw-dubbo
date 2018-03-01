package com.hdw.dubbo.upms.rpc.api;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.dubbo.common.result.PageInfo;
import com.hdw.dubbo.upms.entity.SysLog;

/**
 *
 * SysLog 表数据服务层接口
 *
 */
public interface ISysLogService extends IService<SysLog> {

    void selectDataGrid(PageInfo pageInfo);

}