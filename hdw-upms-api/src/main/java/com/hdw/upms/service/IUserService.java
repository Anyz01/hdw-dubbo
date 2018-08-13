package com.hdw.upms.service;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.common.result.PageInfo;
import com.hdw.upms.entity.User;
import com.hdw.upms.entity.vo.UserVo;

/**
 * User 表数据服务层接口
 */
public interface IUserService extends IService<User> {

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