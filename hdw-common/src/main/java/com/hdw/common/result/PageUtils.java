
package com.hdw.common.result;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hdw.common.csrf.SQLFilter;
import com.hdw.common.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @Description 分页工具类(用于Vue.js)
 * @Author TuMinglong
 * @Date 2018/6/11 19:41
 */
public class PageUtils<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    //总记录数
    private long totalCount;
    //每页记录数
    private int pageSize;
    //总页数
    private long totalPage;
    //当前页数
    private int currPage;
    //列表数据
    private List list;

    /**
     * mybatis-plus分页参数
     */
    @JsonIgnore
    private Page<T> page;
    /**
     * 当前页码
     */
    @JsonIgnore
    private int nowpage = 1;
    /**
     * 每页条数
     */
    @JsonIgnore
    private int pagesize = 10;

    public PageUtils() {
        super();
    }

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * 分页
     */
    public PageUtils(Page<?> page) {
        this.list = page.getRecords();
        this.totalCount = page.getTotal();
        this.pageSize = page.getSize();
        this.currPage = page.getCurrent();
        this.totalPage = page.getPages();
    }

    public PageUtils(Map<String, Object> params) {
        //分页参数
        if (params.get("page") != null) {
            nowpage = Integer.parseInt((String) params.get("page"));
        }
        if (params.get("limit") != null) {
            pagesize = Integer.parseInt((String) params.get("limit"));
        }

        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String sidx = SQLFilter.sqlInject((String) params.get("sidx"));
        String order = SQLFilter.sqlInject((String) params.get("order"));
        System.out.println("nowpage:" + nowpage + " pagesize:" + pagesize);
        //mybatis-plus分页
        this.page = new Page<>(nowpage, pagesize);

        //排序
        if (StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(order)) {
            this.page.setOrderByField(sidx);
            this.page.setAsc("ASC".equalsIgnoreCase(order));
        }
    }

    public Page<T> getPage() {
        return page;
    }

    public int getNowpage() {
        return nowpage;
    }

    public int getPagesize() {
        return pagesize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return JacksonUtils.toJson(this);
    }
}
