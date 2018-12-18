package com.hdw.upms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hdw.upms.entity.SysUserToken;

/**
 * <p>
 * 系统用户Token Mapper 接口
 * </p>
 *
 * @author TuMinglong
 * @since 2018-06-11
 */
public interface SysUserTokenMapper extends BaseMapper<SysUserToken> {

    SysUserToken selectByToken(String token);
}