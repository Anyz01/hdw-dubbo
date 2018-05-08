package com.hdw.upms.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hdw.upms.entity.Resource;
import com.hdw.upms.entity.Role;


/**
 *
 * Role 表数据库控制层接口
 *
 */
public interface RoleMapper extends BaseMapper<Role> {
	
    List<Map<String, Object>> selectRolePage(Pagination page, Map<String, Object> params);

    List<Long> selectResourceIdListByRoleId(@Param("id") Long id);

    List<Resource> selectResourceListByRoleIdList(@Param("list") List<Long> list);
    
    List<Resource> selectResourceListByRoleIdAndPidList(@Param("list") List<Long> list,@Param("pid")Long pid);

    List<Map<Long, String>> selectResourceListByRoleId(@Param("id") Long id);

}