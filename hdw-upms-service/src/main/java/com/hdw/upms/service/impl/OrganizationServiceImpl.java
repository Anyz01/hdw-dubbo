package com.hdw.upms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.Organization;
import com.hdw.upms.mapper.OrganizationMapper;
import com.hdw.upms.service.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;





/**
 *
 * Organization 表数据服务层接口实现类
 *
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;
    
    @Override
    public List<ZTreeNode> selectTree() {
         return organizationMapper.selectTree();
    }

    @Override
    public List<Map<String,Object>> selectTreeGrid(Map<String,Object> par) {
        
        return organizationMapper.selectTreeGrid(par);
    }


}