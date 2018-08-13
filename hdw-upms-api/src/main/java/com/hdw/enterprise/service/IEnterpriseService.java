package com.hdw.enterprise.service;


import com.baomidou.mybatisplus.service.IService;
import com.hdw.common.result.PageInfo;
import com.hdw.common.result.ZTreeNode;
import com.hdw.enterprise.entity.Enterprise;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 企业信息表 服务类
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
public interface IEnterpriseService extends IService<Enterprise> {

    PageInfo selectDataGrid(PageInfo pageInfo);

    /**
     * 企业树
     *
     * @param par
     * @return
     */
    List<ZTreeNode> selectTree(Map<String, Object> par);

    /**
     * 根据Id、名称、用户名查询企业信息
     *
     * @param par
     * @return
     */
    Enterprise selectEnterpriseByMap(Map<String, Object> par);

    /**
     * 根据Id、名称、行业、区域、用户名查询企业信息
     *
     * @param par
     * @return
     */
    List<Enterprise> selectEnterpriseListByMap(Map<String, Object> par);

    /**
     * 根据多行业查询企业Id
     *
     * @param Ids
     * @return
     */
    List<Long> selectEnterpriseListByIndustryIds(Map<String, Object> par);
}
