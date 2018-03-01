package com.hdw.dubbo.upms.rpc.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.dubbo.common.result.PageInfo;
import com.hdw.dubbo.upms.entity.Role;

/**
 *
 * Role 表数据服务层接口
 *
 */
public interface IRoleService extends IService<Role> {

    void selectDataGrid(PageInfo pageInfo);

    Object selectTree();

    List<Long> selectResourceIdListByRoleId(Long id);

    void updateRoleResource(Long id, String resourceIds);

    Map<String, Set<String>> selectResourceMapByUserId(Long userId);

}