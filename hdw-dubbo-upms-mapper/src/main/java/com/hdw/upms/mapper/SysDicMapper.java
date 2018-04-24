package com.hdw.upms.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hdw.upms.entity.SysDic;


/**
 * <p>
  * 数据字典表 Mapper 接口
 * </p>
 *
 * @author TuMinglong
 * @since 2017-11-14
 */
public interface SysDicMapper extends BaseMapper<SysDic> {
	
	List<SysDic> selectSysDicPage(Pagination page, Map<String, Object> params);
	
	List<SysDic> selectSysDicList(Map<String, Object> params);
	
	List<SysDic> selectSysDic(Map<String, Object> par);
}