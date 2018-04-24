package com.hdw.upms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.hdw.common.result.PageInfo;
import com.hdw.upms.entity.Company;
import com.hdw.upms.mapper.CompanyMapper;
import com.hdw.upms.api.ICompanyService;


/** 
* @Author ChenShi 
* @Date 2017年12月26日
* @Version 1.0V
* 类说明  企业信息服务实现类
*/

public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {

	@Autowired
	private CompanyMapper companyMapper;

	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Company> page = new Page<Company>(pageInfo.getNowpage(),pageInfo.getSize());
		String orderField=StringUtils.camelToUnderline(pageInfo.getSort());
		page.setOrderByField(orderField);
		page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
		List<Company> list = companyMapper.selectCompanyPage(page, pageInfo.getCondition());
		pageInfo.setRows(list);
		pageInfo.setTotal(page.getTotal());
	}
}
