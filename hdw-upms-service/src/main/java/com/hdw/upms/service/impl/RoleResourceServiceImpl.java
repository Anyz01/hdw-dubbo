package com.hdw.upms.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hdw.upms.entity.RoleResource;
import com.hdw.upms.mapper.RoleResourceMapper;
import com.hdw.upms.service.IRoleResourceService;


/**
 * RoleResource 表数据服务层接口实现类
 */
@Service(
        application = "${dubbo.application.id}" ,
        protocol = "${dubbo.protocol.id}" ,
        registry = "${dubbo.registry.id}"
        , group = "hdw-upms"
)
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements IRoleResourceService {


}