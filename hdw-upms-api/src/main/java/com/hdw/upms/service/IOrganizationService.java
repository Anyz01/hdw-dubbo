package com.hdw.upms.service;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.Organization;

import java.util.List;
import java.util.Map;


/**
 * Organization 表数据服务层接口
 */
public interface IOrganizationService extends IService<Organization> {

    List<ZTreeNode> selectTree();

    /**
     * 获取部门树表
     *
     * @param par
     * @return
     */
    List<Map<String, Object>> selectTreeGrid(Map<String, Object> par);

}