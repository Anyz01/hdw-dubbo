package com.hdw.upms.service.impl;



import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hdw.upms.entity.UserRole;
import com.hdw.upms.mapper.UserRoleMapper;
import com.hdw.upms.service.IUserRoleService;


/**
 *
 * UserRole 表数据服务层接口实现类
 *
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}",
        group = "hdw-upms"
)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}