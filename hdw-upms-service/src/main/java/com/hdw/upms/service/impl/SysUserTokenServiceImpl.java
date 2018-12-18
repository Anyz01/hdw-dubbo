package com.hdw.upms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.upms.entity.SysUserToken;
import com.hdw.upms.mapper.SysUserTokenMapper;
import com.hdw.upms.service.ISysUserTokenService;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * <p>
 * 系统用户Token 服务实现类
 * </p>
 *
 * @author TuMinglong
 * @since 2018-06-11
 */
@Service
public class SysUserTokenServiceImpl extends ServiceImpl<SysUserTokenMapper, SysUserToken> implements ISysUserTokenService {

    @Override
    public SysUserToken selectByToken(String token) {
        return this.baseMapper.selectByToken(token);
    }
}
