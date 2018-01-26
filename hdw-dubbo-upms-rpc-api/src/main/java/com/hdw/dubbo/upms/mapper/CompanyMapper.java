package com.hdw.dubbo.upms.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hdw.dubbo.upms.entity.Company;


/** 
* @Author ChenShi 
* @Date 2017年12月26日
* @Version 1.0V
* 类说明  企业信息Mapper接口
*/
public interface CompanyMapper extends BaseMapper<Company> {

	List<Company> selectCompanyPage(Pagination page, Map<String, Object> params);
	
}
