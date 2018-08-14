package com.hdw.upms.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hdw.upms.entity.SysRoleResource;
import com.hdw.upms.mapper.SysRoleResourceMapper;
import com.hdw.upms.service.ISysRoleResourceService;


/**
 * SysRoleResource 表数据服务层接口实现类
 */
@Service(
        application = "${dubbo.application.id}" ,
        protocol = "${dubbo.protocol.id}" ,
        registry = "${dubbo.registry.id}"
        , group = "hdw-upms"
)
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRoleResourceMapper, SysRoleResource> implements ISysRoleResourceService {


}