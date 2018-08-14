package com.hdw.upms.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hdw.upms.entity.SysUserRole;
import com.hdw.upms.mapper.SysUserRoleMapper;
import com.hdw.upms.service.ISysUserRoleService;


/**
 * SysUserRole 表数据服务层接口实现类
 */
@Service(
        application = "${dubbo.application.id}" ,
        protocol = "${dubbo.protocol.id}" ,
        registry = "${dubbo.registry.id}" ,
        group = "hdw-upms"
)
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

}