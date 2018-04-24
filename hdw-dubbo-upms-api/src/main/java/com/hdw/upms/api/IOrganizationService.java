package com.hdw.upms.api;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.common.result.Tree;
import com.hdw.upms.entity.Organization;



/**
 *
 * Organization 表数据服务层接口
 *
 */
public interface IOrganizationService extends IService<Organization> {

    List<Tree> selectTree();

    List<Organization> selectTreeGrid();

}