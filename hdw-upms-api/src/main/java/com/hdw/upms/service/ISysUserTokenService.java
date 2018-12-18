package com.hdw.upms.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.upms.entity.SysUserToken;

/**
 * <p>
 * 系统用户Token 服务类
 * </p>
 *
 * @author TuMinglong
 * @since 2018-06-11
 */
public interface ISysUserTokenService extends IService<SysUserToken> {

    SysUserToken selectByToken(String token);
}
