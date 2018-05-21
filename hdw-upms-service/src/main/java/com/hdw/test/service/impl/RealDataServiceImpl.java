package com.hdw.test.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.hdw.common.result.PageInfo;
import com.hdw.test.entity.RealData;
import com.hdw.test.mapper.RealDataMapper;
import com.hdw.test.service.IRealDataService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 实时数据表 服务实现类
 * </p>
 *
 * @author TuMinglong
 * @since 2018-03-08
 */
@Service(version = "1.0.0", application = "${dubbo.application.id}", protocol = "${dubbo.protocol.id}", registry = "${dubbo.registry.id}")
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
