package com.hdw.upms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.result.TreeNode;
import com.hdw.upms.entity.SysDic;
import com.hdw.upms.mapper.SysDicMapper;
import com.hdw.upms.service.ISysDicService;
import com.alibaba.dubbo.config.annotation.Service;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
@Service
public class SysDicServiceImpl extends ServiceImpl<SysDicMapper, SysDic> implements ISysDicService {

    @Override
    public List<Map<String, Object>> selectTreeGrid(Map<String, Object> par) {

        return this.baseMapper.selectTreeGrid(par);
    }

    @Override
    public List<TreeNode> selectTree(Map<String, Object> par) {

        return this.baseMapper.selectTree(par);
    }



    @Override
    public List<Map<String, Object>> selectTreeByParentId(Map<String, Object> par) {

        return this.baseMapper.selectTreeByParentId(par);
    }
}
