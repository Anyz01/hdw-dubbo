package com.hdw.upms.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.SysResource;


/**
 * 
 * @Descriptin SysResource 表数据库控制层接口
 * @author TuMinglong
 * @Date 2018年5月6日 下午3:13:59
 */
public interface SysResourceMapper extends BaseMapper<SysResource> {
	
	List<Map<String,Object>> selectTreeGrid(Map<String,Object> par);
	
	/**
	 * 菜单树
	 * @return
	 */
	List<ZTreeNode> selectMenuTree();

}