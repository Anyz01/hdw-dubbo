package com.hdw.search.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.hdw.common.result.PageInfo;
import com.hdw.search.entity.RealData;
import com.hdw.search.mapper.RealDataMapper;
import com.hdw.search.api.IRealDataService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 实时数据表 服务实现类
 * </p>
 *
 * @author TuMinglong
 * @since 2018-03-08
 */
@Service
public class RealDataServiceImpl extends ServiceImpl<RealDataMapper, RealData> implements IRealDataService {
	
	@Autowired
	private RealDataMapper realDataMapper;

	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<RealData> page = new Page<RealData>(pageInfo.getNowpage(), pageInfo.getSize());
		String orderField = StringUtils.camelToUnderline(pageInfo.getSort());
		page.setOrderByField(orderField);
		page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
		List<RealData> list = realDataMapper.selectRealDataPage(page, pageInfo.getCondition());
		pageInfo.setRows(list);
		pageInfo.setTotal(page.getTotal());
	}
}
