package com.hdw.upms.service;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.SysOrganization;

import java.util.List;
import java.util.Map;


/**
 * SysOrganization 表数据服务层接口
 */
public interface ISysOrganizationService extends IService<SysOrganization> {

    List<ZTreeNode> selectTree();

    /**
     * 获取部门树表
     *
     * @param par
     * @return
     */
    List<Map<String, Object>> selectTreeGrid(Map<String, Object> par);

}