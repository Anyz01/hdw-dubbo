package com.hdw.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdw.upms.entity.SysUser;
import com.hdw.upms.entity.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户表
 * 
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
    * 多表页面信息查询
    * @param page
    * @param params
    * @return
    */
    IPage<Map<String,Object>> selectSysUserPage(Page page, @Param("params") Map<String, Object> params);

    /**
     * 多表信息查询
     * @param par
     * @return
     */
    List<Map<String, Object>> selectSysUserList(Map<String, Object> par);


    UserVo selectByUserId(@Param("userId") Long userId);

    UserVo selectByLoginName(@Param("loginName") String loginName);

    /**
     * 查询用户的所有菜单ID
     * @param userId
     * @return
     */
    List<Long> selectResourceIdListByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的所有权限
     * @param userId
     * @return
     */
    List<String> selectPerms(@Param("userId") Long userId);
	
}
