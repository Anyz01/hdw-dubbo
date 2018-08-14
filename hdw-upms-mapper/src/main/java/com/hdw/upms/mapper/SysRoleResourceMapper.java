package com.hdw.upms.mapper;

import java.io.Serializable;

import com.hdw.upms.entity.SysRoleResource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;


/**
 *
 * SysRoleResource 表数据库控制层接口
 *
 */
public interface SysRoleResourceMapper extends BaseMapper<SysRoleResource> {

    @Select("SELECT e.id AS id FROM t_sys_role r LEFT JOIN t_sys_role_resource e ON r.id = e.role_id WHERE r.id = #{id}")
    Long selectIdListByRoleId(@Param("id") Long id);

    @Delete("DELETE FROM t_sys_role_resource WHERE resource_id = #{resourceId}")
    int deleteByResourceId(@Param("resourceId") Serializable resourceId);

}