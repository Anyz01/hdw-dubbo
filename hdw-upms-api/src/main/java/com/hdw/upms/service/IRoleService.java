package com.hdw.upms.service;

import com.baomidou.mybatisplus.service.IService;
import com.hdw.common.result.PageInfo;
import com.hdw.common.result.Select2Node;
import com.hdw.upms.entity.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Role 表数据服务层接口
 */
public interface IRoleService extends IService<Role> {

    PageInfo selectDataGrid(PageInfo pageInfo);

    List<Select2Node> selectTree();

    /**
     * 根据用户Id获取角色树
     *
     * @param userId
     * @return
     */
    List<Select2Node> selectTreeByUserId(Long userId);

    List<Long> selectResourceIdListByRoleId(Long id);

    void updateRoleResource(Long id, String resourceIds);

    Map<String, Set<String>> selectResourceMapByUserId(Long userId);

}