package com.hdw.upms.service;

import com.hdw.common.result.PageInfo;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.SysDic;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
public interface ISysDicService extends IService<SysDic> {

    PageInfo selectDataGrid(PageInfo pageInfo);

    /**
     * 获取变量树表
     *
     * @param par
     * @return
     */
    List<Map<String, Object>> selectTreeGrid(Map<String, Object> par);

    /**
     * 获取数据字典树
     *
     * @param par
     * @return
     */
    List<ZTreeNode> selectTree(Map<String, Object> par);
}
