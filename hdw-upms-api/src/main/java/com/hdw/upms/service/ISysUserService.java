package com.hdw.upms.service;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.common.result.PageInfo;
import com.hdw.upms.entity.SysUser;
import com.hdw.upms.entity.vo.UserVo;

/**
 * SysUser 表数据服务层接口
 */
public interface ISysUserService extends IService<SysUser> {

    UserVo selectByLoginName(String loginName);

    void insertByVo(UserVo userVo);

    void updateByVo(UserVo userVo);

    UserVo selectVoById(Long id);

    /**
     * 设置角色
     *
     * @param userId
     * @param roleIds
     */
    void setRoles(Long userId, String roleIds);

    void updatePwdByUserId(Long userId, String md5Hex);

    PageInfo selectDataGrid(PageInfo pageInfo);

    void deleteUserById(Long id);
}