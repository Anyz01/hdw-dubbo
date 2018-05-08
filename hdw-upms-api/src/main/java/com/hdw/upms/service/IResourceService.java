package com.hdw.upms.service;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.common.result.MenuNode;

import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.Resource;
import com.hdw.upms.entity.vo.UserVo;

import java.util.List;
import java.util.Map;


/**
 *
 * Resource 表数据服务层接口
 *
 */
public interface IResourceService extends IService<Resource> {
    
	/**
	 * 根据资源树
	 * @return
	 */
    List<ZTreeNode> selectTree();
    
    /**
     * 根据用户获取资源树
     * @param userVo 用户
     * @return
     */
    List<MenuNode> selectTree(UserVo userVo);
    
    /**
     * 获取资源树表
     * @param par
     * @return
     */
    List<Map<String,Object>> selectTreeGrid(Map<String,Object> par);
    
    /**
     * 获取菜单
     * @return
     */
    List<MenuNode> selectMenu();
    
    /**
	 * 菜单树
	 * @return
	 */
	List<ZTreeNode> selectMenuTree();

  

}