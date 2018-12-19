package com.hdw.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hdw.upms.entity.SysResource;

import java.util.List;
import java.util.Map;

/**
 * 资源表
 * 
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<SysResource> selectResourceList(Map<String, Object> params);

}
