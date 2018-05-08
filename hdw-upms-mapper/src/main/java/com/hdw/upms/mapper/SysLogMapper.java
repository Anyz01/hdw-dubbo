package com.hdw.upms.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hdw.upms.entity.SysLog;


/**
 *
 * SysLog 表数据库控制层接口
 *
 */
public interface SysLogMapper extends BaseMapper<SysLog> {
	
	 List<SysLog> selectLogPage(@Param("page")Pagination page, Map<String, Object> params);

}