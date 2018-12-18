package com.hdw.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.result.PageUtils;
import com.hdw.upms.entity.SysRole;
import com.hdw.upms.entity.vo.RoleVo;

import java.util.List;
import java.util.Map;

/**
 * 角色表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
public interface ISysRoleService extends IService<SysRole> {

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
    List<SysRole> selectSysRoleList(Map<String, Object> par);

    RoleVo selectByUserId(Long userId);

    RoleVo selectByRoleId(Long roleId);

    void saveByVo(SysRole role);

    void updateByVo(SysRole role);

    void deleteBatch(Long[] roleIds);
}

