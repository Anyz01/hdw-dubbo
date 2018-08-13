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
 * @author TuMinglong
 * @Descriptin PageInfo 分页实体类 (结合EasyUI、Jquery Table)
 * @Date 2018年5月7日 下午4:23:03
 */
@SuppressWarnings("rawtypes")
public class PageInfo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static int PAGESIZE = 10; //默认显示的记录数

    private long total; // 总记录 
    private List rows; //显示的记录

    @JsonIgnore
    private int from;
    @JsonIgnore
    private int size;
    @JsonIgnore
    private int nowpage; // 当前页 
    @JsonIgnore
    private int pagesize; // 每页显示的记录数
    @JsonIgnore
    private String sort = "seq";// 排序字段
    @JsonIgnore
    private String order = "asc";// asc，desc mybatis Order 关键字
    @JsonIgnore
    private Map<String, Object> condition; //查询条件
    /**
     * mybatis-plus分页参数
     */
    @JsonIgnore
    private Page<T> page;

    public PageInfo() {
    }

    // 构造方法
    public PageInfo(int nowpage, int pagesize, String sort, String order) {
        //计算当前页
        if (nowpage < 0) {
            this.nowpage = 1;
        } else {
            //当前页
            this.nowpage = nowpage / pagesize + 1;
        }
        //记录每页显示的记录数
        if (pagesize < 0) {
            this.pagesize = PAGESIZE;
        } else {
            this.pagesize = pagesize;
        }
        //计算开始的记录和结束的记录
        this.from = (this.nowpage - 1) * this.pagesize;
        this.size = this.pagesize;

        //mybatis-plus分页
        this.page = new Page<>(this.nowpage, this.pagesize);

        this.sort = sort;
        this.order = order;
        //排序
        if (StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)) {
            //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
            this.sort = SQLFilter.sqlInject(this.sort);
            this.order = SQLFilter.sqlInject(this.order);

            this.page.setOrderByField(this.sort);
            this.page.setAsc("ASC".equalsIgnoreCase(this.order));
        }
    }

    //构造方法
    public PageInfo(int nowpage, int pagesize) {
        this(nowpage, pagesize, "", "");
    }

    public PageInfo(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNowpage() {
        return nowpage;
    }

    public void setNowpage(int nowpage) {
        this.nowpage = nowpage;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return JacksonUtils.toJson(this);
    }
}
