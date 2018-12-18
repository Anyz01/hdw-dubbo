package com.hdw.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hdw.common.result.TreeNode;
import com.hdw.enterprise.entity.EnterpriseDepartment;

import java.util.List;
import java.util.Map;

/**
 * 企业部门表
 * 
 * @author TuMinglong
 * @date 2018-12-11 11:36:02
 */
public interface EnterpriseDepartmentMapper extends BaseMapper<EnterpriseDepartment> {
    /**
     * 树表
     * @param params
     * @return
     */
    List<Map<String, Object>> selectTreeGrid(Map<String, Object> params);

    /**
     * 根据企业部门获取部门树
     *
     * @param params
     * @return
     */
    List<TreeNode> selectTree(Map<String, Object> params);
	
}
