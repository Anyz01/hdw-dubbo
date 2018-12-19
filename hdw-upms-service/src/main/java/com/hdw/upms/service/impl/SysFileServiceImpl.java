package com.hdw.upms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.result.PageUtils;
import com.hdw.upms.entity.SysFile;
import com.hdw.upms.mapper.SysFileMapper;
import com.hdw.upms.service.ISysFileService;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;
import java.util.Map;

/**
 * 附件表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

    @Override
    public List<SysFile> selectFileListByTableIdAndRecordId(Map<String, Object> params) {
        return this.baseMapper.selectFileListByTableIdAndRecordId(params);
    }

    @Override
    public PageUtils<SysFile> selectSysFilePage(Map<String, Object> params) {
        Page<SysFile> page = new PageUtils<SysFile>(params).getPage();
        IPage<SysFile> iPage = this.baseMapper.selectSysFilePage(page, params);
        return new PageUtils<SysFile>(iPage);
    }
}
