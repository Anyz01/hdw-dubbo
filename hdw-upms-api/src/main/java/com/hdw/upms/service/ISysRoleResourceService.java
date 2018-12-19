package com.hdw.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.result.SelectTreeNode;
import com.hdw.upms.entity.SysRoleResource;

import java.util.List;


/**
 * 角色资源表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
public interface ISysRoleResourceService extends IService<SysRoleResource> {

    /**
     * 根据角色查找菜单ID集合
     * @param roleId
     * @return
     */
   List<Long> selectResourceIdListByRoleId(Long roleId);

   void saveOrUpdateRoleResource(Long roleId, List<Long> resourceIdList);

   void deleteBatch(Long[] roleIds);

    List<SelectTreeNode> selectResourceNodeListByRoleId(Long roleId);

}

