package com.hdw.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.result.PageUtils;
import com.hdw.upms.entity.SysUser;
import com.hdw.upms.entity.vo.UserVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
public interface ISysUserService extends IService<SysUser> {

    /**
    * 分页查询
    * @param params
    * @return
    */
    PageUtils selectDataGrid(Map<String, Object> params);

    /**
    * 多表信息查询
    * @param par
    * @return
    */
    List<Map<String, Object>> selectSysUserList(Map<String, Object> par);

    UserVo selectByUserId(Long userId);

    UserVo selectByLoginName(String loginName);

    void saveByVo(SysUser user);

    void updateByVo(SysUser user);

    /**
     *  查询用户的所有菜单ID
     * @param userId
     * @return
     */
    List<Long> selectResourceIdListByUserId(Long userId);

    /**
     * 根据用户批量删除
     * @param userIds
     */
    void deleteBatch(Long[] userIds);

    /**
     * 获取用户权限列表
     */
    Set<String> selectUserPermissions(long userId);

}

