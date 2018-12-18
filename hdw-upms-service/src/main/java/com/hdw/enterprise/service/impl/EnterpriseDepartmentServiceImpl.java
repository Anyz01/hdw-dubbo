package com.hdw.enterprise.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.result.TreeNode;
import com.hdw.enterprise.entity.EnterpriseDepartment;
import com.hdw.enterprise.mapper.EnterpriseDepartmentMapper;
import com.hdw.enterprise.service.IEnterpriseDepartmentService;

import java.util.List;
import java.util.Map;

/**
 * 企业部门表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:36:02
 */
@Service
public class EnterpriseDepartmentServiceImpl extends ServiceImpl<EnterpriseDepartmentMapper, EnterpriseDepartment> implements IEnterpriseDepartmentService {


    @Override
    public List<Map<String, Object>> selectTreeGrid(Map<String, Object> params) {
        return this.baseMapper.selectTreeGrid(params);
    }

    @Override
    public List<TreeNode> selectTree(Map<String, Object> params) {
        return this.baseMapper.selectTree(params);
    }
}
