package com.hdw.upms.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hdw.upms.entity.SysDic;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 数据字典 Mapper 接口
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
public interface SysDicMapper extends BaseMapper<SysDic> {
	List<SysDic> selectSysDicPage(Pagination page, Map<String, Object> params);
}