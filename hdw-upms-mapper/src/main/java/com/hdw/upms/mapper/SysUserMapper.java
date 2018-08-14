package com.hdw.upms.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hdw.upms.entity.SysUser;
import com.hdw.upms.entity.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 *
 * SysUser 表数据库控制层接口
 *
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	
	UserVo selectByLoginName(String loginName);

    UserVo selectUserVoById(@Param("id") Long id);

    List<Map<String, Object>> selectUserPage(Pagination page, Map<String, Object> params);

}