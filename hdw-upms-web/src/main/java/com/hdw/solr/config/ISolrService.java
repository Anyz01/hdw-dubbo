package com.hdw.solr.config;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TuMinglong
 * @Description com.hdw.solr.config
 * @date 2018/8/16 22:04
 */
public interface ISolrService {

    /**
     * 创建索引
     *
     * @param list
     */
    void createIndex(List<Map<String, Object>> list);

    /**
     * 根据索引删除 实例 "id:1"
     *
     * @param query
     */
    void deleteByQuery(String query);

    /**
     * 删除所有
     */
    void deleteAll();

    /**
     * 查询所有
     *
     * @param highlight 是否高亮
     * @param list      搜索条件对象集合
     * @return
     */
    List<Map<String, Object>> queryList(boolean highlight, List<SolrObject> list);

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param highlight 是否高亮
     * @param list      搜索条件对象集合
     * @return
     */
    List<Map<String, Object>> queryList(Integer pageNum, Integer pageSize, boolean highlight, List<SolrObject> list);

}
