package com.hdw.dubbo.upms.rpc.api;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.dubbo.common.result.Tree;
import com.hdw.dubbo.upms.entity.Resource;
import com.hdw.dubbo.upms.entity.ShiroUser;



/**
 *
 * Resource 表数据服务层接口
 *
 */
public interface IResourceService extends IService<Resource> {

    List<Resource> selectAll();

    List<Tree> selectAllMenu();

    List<Tree> selectAllTree();

    List<Tree> selectTree(ShiroUser shiroUser);

}