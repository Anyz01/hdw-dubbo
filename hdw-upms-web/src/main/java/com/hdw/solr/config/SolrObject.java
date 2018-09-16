package com.hdw.solr.config;

import com.hdw.common.util.JacksonUtils;

import java.io.Serializable;

/**
 * @Description solr搜索条件对象
 * @Date 2018/8/29 14:43
 * @Author TuMinglong
 */
public class SolrObject implements Serializable {

    /**
     * 搜索字段名称
     */
    private String name;

    /**
     * 搜索字段内容
     */
    private String query;

    /**
     * 是否参与查询
     * 默认 是
     */
    private boolean isQuery = true;

    /**
     * 是否参与排序
     * 默认不排序
     * n 代表不排序
     * yd 排序并且是desc
     * ya 排序并且是asc
     */
    private String isSort = "n";

    /**
     * 是否使用运算符
     * 默认 否
     * 是 直接填写要使用的符号
     * 否 n
     */
    private String queryType = "n";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean getIsQuery() {
        return isQuery;
    }

    public void setIsQuery(boolean query) {
        isQuery = query;
    }

    public String getIsSort() {
        return isSort;
    }

    public void setIsSort(String isSort) {
        isSort = isSort;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    @Override
    public String toString() {
        return JacksonUtils.toJson(this);
    }
}
