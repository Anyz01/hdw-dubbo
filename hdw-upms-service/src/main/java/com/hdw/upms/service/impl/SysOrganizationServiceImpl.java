package com.hdw.upms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.SysOrganization;
import com.hdw.upms.mapper.SysOrganizationMapper;
import com.hdw.upms.service.ISysOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


/**
 * SysOrganization 表数据服务层接口实现类
 */
@Service(
        application = "${dubbo.application.id}" ,
        protocol = "${dubbo.protocol.id}" ,
        registry = "${dubbo.registry.id}" ,
        group = "hdw-upms"
)
public class SysOrganizationServiceImpl extends ServiceImpl<SysOrganizationMapper, SysOrganization> implements ISysOrganizationService {

    @Autowired
    private SysOrganizationMapper organizationMapper;

    @Override
    public List<ZTreeNode> selectTree() {
        return organizationMapper.selectTree();
    }

    @Override
    public List<Map<String, Object>> selectTreeGrid(Map<String, Object> par) {

        return organizationMapper.selectTreeGrid(par);
    }


}