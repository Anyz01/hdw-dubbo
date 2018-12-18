package com.hdw.upms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.result.PageUtils;
import com.hdw.sys.entity.SysLog;
import com.hdw.sys.mapper.SysLogMapper;
import com.hdw.sys.service.ISysLogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统日志表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public PageUtils selectDataGrid(Map<String, Object> params){
        Page<SysLog> page = new PageUtils<SysLog>(params).getPage();
        IPage<SysLog> iPage = this.baseMapper.selectSysLogPage(page, params);
        return new PageUtils<Map<String, Object>>(iPage);
    }

    @Override
    public List<SysLog> selectSysLogList(Map<String, Object> par){

        return this.baseMapper.selectSysLogList(par);
    }

}
