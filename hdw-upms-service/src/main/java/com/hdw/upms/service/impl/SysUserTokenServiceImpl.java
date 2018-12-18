package com.hdw.upms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.sys.entity.SysUserToken;
import com.hdw.sys.mapper.SysUserTokenMapper;
import com.hdw.sys.service.ISysUserTokenService;
import org.springframework.stereotype.Service;

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
