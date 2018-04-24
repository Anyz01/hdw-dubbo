package com.hdw.upms.service.impl;



import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hdw.common.result.PageInfo;
import com.hdw.upms.entity.SysLog;
import com.hdw.upms.mapper.SysLogMapper;
import com.hdw.upms.api.ISysLogService;


/**
 *
 * SysLog 表数据服务层接口实现类
 *
 */
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {
    
    @Override
    public void selectDataGrid(PageInfo pageInfo) {
        Page<SysLog> page = new Page<SysLog>(pageInfo.getNowpage(), pageInfo.getSize());
        EntityWrapper<SysLog> wrapper = new EntityWrapper<SysLog>();
        wrapper.orderBy(pageInfo.getSort(), pageInfo.getOrder().equalsIgnoreCase("ASC"));
        selectPage(page, wrapper);
        pageInfo.setRows(page.getRecords());
        pageInfo.setTotal(page.getTotal());
    }
    
}