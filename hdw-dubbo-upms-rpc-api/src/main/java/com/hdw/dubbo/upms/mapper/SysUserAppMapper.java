package com.hdw.dubbo.upms.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hdw.dubbo.upms.entity.SysUserApp;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 用户与应用关系表 Mapper 接口
 * </p>
 *
 * @author TuMinglong
 * @since 2018-03-07
 */
public interface SysUserAppMapper extends BaseMapper<SysUserApp> {
	List<SysUserApp> selectSysUserAppPage(Pagination page, Map<String, Object> params);
}