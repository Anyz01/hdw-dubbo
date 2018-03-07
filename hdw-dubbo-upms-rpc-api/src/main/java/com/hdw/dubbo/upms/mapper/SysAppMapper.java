package com.hdw.dubbo.upms.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hdw.dubbo.upms.entity.SysApp;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 应用 Mapper 接口
 * </p>
 *
 * @author TuMinglong
 * @since 2018-03-07
 */
public interface SysAppMapper extends BaseMapper<SysApp> {
	List<SysApp> selectSysAppPage(Pagination page, Map<String, Object> params);
}