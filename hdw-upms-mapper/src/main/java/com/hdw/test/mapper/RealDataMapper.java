package com.hdw.test.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hdw.test.entity.RealData;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 实时数据表 Mapper 接口
 * </p>
 *
 * @author TuMinglong
 * @since 2018-03-08
 */
public interface RealDataMapper extends BaseMapper<RealData> {
	List<RealData> selectRealDataPage(Pagination page, Map<String, Object> params);
}