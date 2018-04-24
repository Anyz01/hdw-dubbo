package com.hdw.upms.api;



import com.baomidou.mybatisplus.service.IService;
import com.hdw.common.result.PageInfo;
import com.hdw.upms.entity.Company;


/** 
* @Author ChenShi 
* @Date 2017年12月26日
* @Version 1.0V
* 类说明 企业信息服务类
*/
public interface ICompanyService extends IService<Company> {

	void selectDataGrid(PageInfo pageInfo);
	
}
