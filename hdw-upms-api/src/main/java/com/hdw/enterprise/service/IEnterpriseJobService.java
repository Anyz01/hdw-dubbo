package com.hdw.enterprise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.result.PageUtils;
import com.hdw.common.result.TreeNode;
import com.hdw.enterprise.entity.EnterpriseJob;

import java.util.List;
import java.util.Map;

/**
 * 企业职务配置表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:36:02
 */
public interface IEnterpriseJobService extends IService<EnterpriseJob> {

    /**
    * 多表页面信息查询
    * @param params
    * @return
    */
    PageUtils selectDataGrid(Map<String, Object> params);

    /**
    * 多表信息查询
    * @param params
    * @return
    */
    List<Map<String, Object>> selectEnterpriseJobList(Map<String, Object> params);

    /**
     * 根据企业职位获取职位树
     *
     * @param params
     * @return
     */
    List<TreeNode> selectTree(Map<String, Object> params);


}

