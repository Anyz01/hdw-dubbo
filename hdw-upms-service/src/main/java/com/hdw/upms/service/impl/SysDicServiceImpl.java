package com.hdw.upms.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.hdw.common.result.PageInfo;
import com.hdw.common.result.ZTreeNode;
import com.hdw.upms.entity.SysDic;
import com.hdw.upms.mapper.SysDicMapper;
import com.hdw.upms.service.ISysDicService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
@Service(
        application = "${dubbo.application.id}" ,
        protocol = "${dubbo.protocol.id}" ,
        registry = "${dubbo.registry.id}" ,
        group = "hdw-upms"
)
public class SysDicServiceImpl extends ServiceImpl<SysDicMapper, SysDic> implements ISysDicService {

    @Autowired
    private SysDicMapper sysDicMapper;

    @Override
    public PageInfo selectDataGrid(PageInfo pageInfo) {
        Page<SysDic> page = new Page<SysDic>(pageInfo.getNowpage(), pageInfo.getSize());
        String orderField = StringUtils.camelToUnderline(pageInfo.getSort());
        page.setOrderByField(orderField);
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<SysDic> list = sysDicMapper.selectSysDicPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    @Override
    public List<Map<String, Object>> selectTreeGrid(Map<String, Object> par) {

        return sysDicMapper.selectTreeGrid(par);
    }

    @Override
    public List<ZTreeNode> selectTree(Map<String, Object> par) {

        return sysDicMapper.selectTree(par);
    }
}
