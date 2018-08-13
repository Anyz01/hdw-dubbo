package com.hdw.enterprise.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.hdw.common.result.PageInfo;
import com.hdw.common.result.ZTreeNode;
import com.hdw.enterprise.entity.Enterprise;
import com.hdw.enterprise.mapper.EnterpriseMapper;
import com.hdw.enterprise.service.IEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 企业信息表 服务实现类
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
@Service(
        application = "${dubbo.application.id}" ,
        protocol = "${dubbo.protocol.id}" ,
        registry = "${dubbo.registry.id}" ,
        group = "hdw-upms" ,
        timeout = 60000
)
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements IEnterpriseService {

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Override
    public PageInfo selectDataGrid(PageInfo pageInfo) {
        Page<Enterprise> page = new Page<Enterprise>(pageInfo.getNowpage(), pageInfo.getSize());
        String orderField = StringUtils.camelToUnderline(pageInfo.getSort());
        page.setOrderByField(orderField);
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<Enterprise> list = enterpriseMapper.selectEnterprisePage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    @Override
    public List<ZTreeNode> selectTree(Map<String, Object> par) {
        List<ZTreeNode> trees = new ArrayList<>();
        List<Enterprise> list = enterpriseMapper.selectEnterpriseListByMap(par);
        if (list != null && !list.isEmpty()) {
            for (Enterprise e : list) {
                ZTreeNode tree = new ZTreeNode();
                tree.setId(e.getId());
                tree.setpId(0l);
                tree.setName(e.getEnterpriseName());
                tree.setOpen(false);
                trees.add(tree);
            }
        }
        return trees;
    }

    @Override
    public Enterprise selectEnterpriseByMap(Map<String, Object> par) {
        return enterpriseMapper.selectEnterpriseByMap(par);
    }

    @Override
    public List<Enterprise> selectEnterpriseListByMap(Map<String, Object> par) {
        return enterpriseMapper.selectEnterpriseListByMap(par);
    }

    @Override
    public List<Long> selectEnterpriseListByIndustryIds(Map<String, Object> par) {

        return enterpriseMapper.selectEnterpriseListByIndustryIds(par);
    }
}
