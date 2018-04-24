package com.hdw.search.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;


/**
 * 
 * @description solr存储请求类
 * @author TuMinglong
 * @date 2018年3月8日 下午3:44:44
 */
public class PushDataIntoSolrRequest implements Serializable{

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
	private Map<String,Object> doc;
	
	@NotNull
	private List<Map<String,Object>> list;
	
	public String getCoreName() {
		return coreName;
	}

	public void setCoreName(String coreName) {
		this.coreName = coreName;
	}

	public Map<String, Object> getDoc() {
		return doc;
	}

	public void setDoc(Map<String, Object> doc) {
		this.doc = doc;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	
}
