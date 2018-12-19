package com.hdw.upms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.util.Constant;
import com.hdw.upms.entity.SysResource;
import com.hdw.upms.mapper.SysResourceMapper;
import com.hdw.upms.service.ISysResourceService;
import com.hdw.upms.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 资源表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
@Service
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements ISysResourceService {

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public List<SysResource> selectResourceList(Map<String, Object> params) {
        return this.baseMapper.selectResourceList(params);
    }

    @Override
    public List<SysResource> selectUserResourceListByUserId(Long userId) {
        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            return selectMenuList(null);
        }
        //用户菜单列表
        List<Long> menuIdList = sysUserService.selectResourceIdListByUserId(userId);
        return selectMenuList(menuIdList);
    }


    @Override
    public List<SysResource> selectListByParentId(Long parentId, List<Long> menuIdList) {
        List<SysResource> menuList = selectListByParentId(parentId);
        if(menuIdList == null){
            return menuList;
        }

        List<SysResource> userMenuList = new ArrayList<>();
        for(SysResource menu : menuList){
            if(menuIdList.contains(menu.getId())){
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<SysResource> selectListByParentId(Long parentId) {
        QueryWrapper<SysResource> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId)
                .eq("status",0)
                .orderByAsc(new String[]{"seq"});
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysResource> selectNotButtonList() {
        QueryWrapper<SysResource> queryWrapper=new QueryWrapper<>();
        queryWrapper.ne("resource_type",2)
                .eq("status",0)
                .orderByAsc(new String[]{"seq"});
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysResource> selectMenuList(List<Long> menuIdList){
        //查询根菜单列表
        List<SysResource> menuList = selectListByParentId(0L, menuIdList);
        //递归获取子菜单
        selectMenuTreeList(menuList, menuIdList);
        return menuList;
    }

    /**
     * 递归
     */
    private List<SysResource> selectMenuTreeList(List<SysResource> menuList, List<Long> menuIdList){
        List<SysResource> subMenuList = new ArrayList<>();
        for(SysResource entity : menuList){
            //目录
            if(entity.getResourceType() == Constant.MenuType.CATALOG.getValue()){
                entity.setList(selectMenuTreeList(selectListByParentId(entity.getId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }
        return subMenuList;
    }
}
