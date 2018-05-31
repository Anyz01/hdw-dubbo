package com.hdw.enterprise.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hdw.enterprise.entity.Enterprise;

import java.util.List;
import java.util.Map;


/**
 * <p>
  * 企业信息表 Mapper 接口
 * </p>
 *
 * @author TuMinglong
 * @since 2018-04-26
 */
public interface EnterpriseMapper extends BaseMapper<Enterprise> {

	List<Enterprise> selectEnterprisePage(Pagination page, Map<String, Object> params);


	/**
	 * 根据Id、名称、用户名查询企业信息
	 * @param par
	 * @return
	 */
	Enterprise selectEnterpriseByMap(Map<String, Object> par);

	/**
	 * 根据Id、名称、行业、区域、用户名查询企业信息
	 * @param par
	 * @return
	 */
	List<Enterprise> selectEnterpriseListByMap(Map<String, Object> par);
	
	/**
	 * 根据多行业查询企业Id
	 * @param Ids
	 * @return
	 */
	List<Long> selectEnterpriseListByIndustryIds(Map<String, Object> par);
}