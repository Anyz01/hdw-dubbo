package com.hdw.search.entity;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 * 
 * @description solr查询请求类
 * @author TuMinglong
 * @date 2018年3月8日 下午3:44:44
 */
public class QuerySolrIndexRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 核心名称
	 */
	@NotNull
	private String coreName;
	
	/**
	 * 存储实体
	 */
	@NotNull
	private String query;

	public String getCoreName() {
		return coreName;
	}

	public void setCoreName(String coreName) {
		this.coreName = coreName;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
