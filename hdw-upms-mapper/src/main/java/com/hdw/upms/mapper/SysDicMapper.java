package com.hdw.upms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hdw.common.result.TreeNode;
import com.hdw.upms.entity.SysDic;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
public interface SysDicMapper extends BaseMapper<SysDic> {

    /**
     * 获取变量树表
     *
     * @return
     */
    List<Map<String, Object>> selectTreeGrid(Map<String, Object> par);

    /**
     * 获取数据字典树
     *
     * @param par
     * @return
     */
    List<TreeNode> selectTree(Map<String, Object> par);

    /**
     * 根据父ID获取变量
     * @param par
     * @return
     */
    List<Map<String, Object>> selectTreeByParentId(Map<String, Object> par);

}