package com.hdw.dubbo.search.rpc.api;


import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.hdw.dubbo.search.entity.PushDataIntoSolrRequest;





/**
 * 
 * @description SearchApi接口层
 * @author TuMinglong
 * @date 2018年3月8日 上午10:59:10
 */
public interface ISearchApiService {

	/**
	 * 向solr中插入数据
	 * 
	 * @param coreName
	 *            核心名称
	 * @param inputDocument
	 * @return
	 */
	public <T> boolean insertOfSolr(String coreName, T t);
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public boolean insertOfSolr(PushDataIntoSolrRequest request);

	/**
	 * 批量向solr中插入数据
	 * 
	 * @param coreName
	 *            核心名称
	 * @param inputDocument
	 * @return
	 */
	public <T> boolean insertBatchOfSolr(String coreName, List<T>  t);
	
	/**
	 * 批量向solr中插入数据
	 * @param request
	 * @return
	 */
	public boolean insertBatchOfSolr(PushDataIntoSolrRequest request);

	/**
	 * 更新索引
	 * 
	 * @param coreName
	 * @param input
	 * @return
	 */
	public <T> boolean updateOfSolr(String coreName, T t);

	/**
	 * 删除索引
	 * 
	 * @param value
	 */
	public boolean deleteByIdOfSolr(String coreName,String id);

	
    /**
     * 根据条件查询
     * @param coreName
     * @param solrQuery
     * @return
     */
	public QueryResponse queryOfSolr(String coreName, SolrQuery solrQuery) ; 
	
	/**
	 * 清空所有
	 * @return
	 */
	public boolean DeleteAllOfSolr(String coreName);
	
	
}
