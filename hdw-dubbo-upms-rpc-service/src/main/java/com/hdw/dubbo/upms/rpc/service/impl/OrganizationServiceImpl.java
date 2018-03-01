package com.hdw.dubbo.upms.rpc.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hdw.dubbo.common.result.Tree;
import com.hdw.dubbo.upms.entity.Organization;
import com.hdw.dubbo.upms.mapper.OrganizationMapper;
import com.hdw.dubbo.upms.rpc.api.IOrganizationService;





/**
 *
 * Organization 表数据服务层接口实现类
 *
 */

public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;
    
    @Override
    public List<Tree> selectTree() {
        List<Organization> organizationList = selectTreeGrid();

        List<Tree> trees = new ArrayList<Tree>();
        if (organizationList != null) {
            for (Organization organization : organizationList) {
                Tree tree = new Tree();
                tree.setId(organization.getId());
                tree.setText(organization.getName());
                tree.setIconCls(organization.getIcon());
                tree.setPid(organization.getPid());
                trees.add(tree);
            }
        }
        return trees;
    }

    @Override
    public List<Organization> selectTreeGrid() {
        EntityWrapper<Organization> wrapper = new EntityWrapper<Organization>();
        wrapper.orderBy("seq");
        return organizationMapper.selectList(wrapper);
    }


}