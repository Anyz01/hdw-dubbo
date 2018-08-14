package com.hdw.upms.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.SysOrganization;

import java.util.List;
import java.util.Map;


/**
 *
 * SysOrganization 表数据库控制层接口
 *
 */
public interface SysOrganizationMapper extends BaseMapper<SysOrganization> {
    /**
     * 获取ztree的节点列表
     * @return
     */
    List<ZTreeNode> selectTree();
    
    /**
     * 获取部门树表
     * @return
     */
    List<Map<String,Object>> selectTreeGrid(Map<String,Object> par);

}