package com.hdw.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdw.upms.entity.SysRole;
import com.hdw.upms.entity.vo.RoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色表
 * 
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
    * 多表页面信息查询
    * @param page
    * @param params
    * @return
    */
    IPage<SysRole> selectSysRolePage(Page page, @Param("params") Map<String, Object> params);

    /**
     * 多表信息查询
     * @param params
     * @return
     */
    List<SysRole> selectSysRoleList(Map<String, Object> params);

    RoleVo selectByUserId(@Param("userId") Long userId);

    RoleVo selectByRoleId(@Param("roleId") Long roleId);

	
}
