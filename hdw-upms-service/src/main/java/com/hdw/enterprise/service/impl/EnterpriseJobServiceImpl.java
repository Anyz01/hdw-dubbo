package com.hdw.enterprise.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.result.PageUtils;
import com.hdw.common.result.TreeNode;
import com.hdw.enterprise.entity.EnterpriseJob;
import com.hdw.enterprise.mapper.EnterpriseJobMapper;
import com.hdw.enterprise.service.IEnterpriseJobService;
import java.util.List;
import java.util.Map;

/**
 * 企业职务配置表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:36:02
 */
@Service
public class EnterpriseJobServiceImpl extends ServiceImpl<EnterpriseJobMapper, EnterpriseJob> implements IEnterpriseJobService {

    @Override
    public PageUtils selectDataGrid(Map<String, Object> params){
        Page<Map<String, Object>> page = new PageUtils<Map<String, Object>>(params).getPage();
        IPage<Map<String, Object>> iPage= this.baseMapper.selectEnterpriseJobPage(page, params);
        return new PageUtils<Map<String, Object>>(iPage);
    }

    @Override
    public List<Map<String, Object>> selectEnterpriseJobList(Map<String, Object> par){

        return this.baseMapper.selectEnterpriseJobList(par);
    }

    @Override
    public List<TreeNode> selectTree(Map<String, Object> params) {
        return this.baseMapper.selectTree(params);
    }

}
