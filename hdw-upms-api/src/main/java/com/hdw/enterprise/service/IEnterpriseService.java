package com.hdw.enterprise.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.result.PageUtils;
import com.hdw.enterprise.entity.Enterprise;

import java.util.List;
import java.util.Map;

/**
 * 企业信息表
 *
 * @author TuMinglong
 * @date 2018-12-11 13:49:00
 */
public interface IEnterpriseService extends IService<Enterprise> {

    /**
    * 多表页面信息查询
    * @param params
    * @return
    */
    PageUtils selectDataGrid(Map<String, Object> params);

    /**
    * 多表信息查询
    * @param par
    * @return
    */
    List<Map<String, Object>> selectEnterpriseList(Map<String, Object> par);


}

