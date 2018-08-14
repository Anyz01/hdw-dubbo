package com.hdw.upms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hdw.upms.entity.SysUserRole;


/**
 *
 * SysUserRole 表数据库控制层接口
 *
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<SysUserRole> selectByUserId(@Param("userId") Long userId);

    @Select("select role_id AS roleId from t_sys_user_role where user_id = #{userId}")
    @ResultType(Long.class)
    List<Long> selectRoleIdListByUserId(@Param("userId") Long userId);

    @Delete("DELETE FROM t_sys_user_role WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

}