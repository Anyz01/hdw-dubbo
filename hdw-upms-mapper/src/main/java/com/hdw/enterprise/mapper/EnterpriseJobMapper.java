package com.hdw.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdw.common.result.TreeNode;
import com.hdw.enterprise.entity.EnterpriseJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 企业职务配置表
 * 
 * @author TuMinglong
 * @date 2018-12-11 11:36:02
 */
public interface EnterpriseJobMapper extends BaseMapper<EnterpriseJob> {

    /**
    * 多表页面信息查询
    * @param page
    * @param params
    * @return
    */
    IPage<Map<String, Object>> selectEnterpriseJobPage(Page page, @Param("params") Map<String, Object> params);

    /**
     * 多表信息查询
     * @param params
     * @return
     */
    List<Map<String, Object>> selectEnterpriseJobList(@Param("params") Map<String, Object> params);

    /**
     * 根据企业职位获取职位树
     *
     * @param params
     * @return
     */
    List<TreeNode> selectTree(Map<String, Object> params);
	
}
