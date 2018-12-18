
package com.hdw.common.result;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hdw.common.util.JacksonUtils;
import com.hdw.common.xss.SQLFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @Description 分页工具类(用于Vue.js) 适用于mybatis-plus 3.0版本
 * @Author TuMinglong
 * @Date 2018/6/11 19:41
 */
public class PageUtils<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    //总记录数
    private long totalCount;
    //每页记录数
    private long pageSize;
    //总页数
    private long totalPage;
    //当前页数
    private long currPage;
    //列表数据
    private List list;

    /**
     * mybatis-plus分页参数
     */
    @JsonIgnore
    private Page<T> page;

    public PageUtils() {
        super();
    }

    /**
     * 分页
     */
    public PageUtils(IPage<?> page) {
        this.list = page.getRecords();
        this.totalCount = page.getTotal();
        this.pageSize = page.getSize();
        this.currPage = page.getCurrent();
        this.totalPage = page.getPages();
    }

    public PageUtils(Map<String, Object> params) {
        //当前页码
         Long page = 1L;
        //当前行数
         Long limit = 10L;
        //分页参数
        if (params.get("page") != null) {
            page =Long.valueOf(String.valueOf(params.get("page")));
        }
        if (params.get("limit") != null) {
            limit = Long.valueOf(String.valueOf(params.get("limit")));
        }

        //防止SQL注入（因为asc、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String asc = SQLFilter.sqlInject((String) params.get("asc"));
        String desc = SQLFilter.sqlInject((String) params.get("desc"));
        //mybatis-plus分页
        this.page = new Page<>(page, limit);

        //排序
        if (StringUtils.isNotBlank(asc) && StringUtils.isNotBlank(desc)) {
            if(asc.indexOf(",")>-1){
                String[] ascs=asc.split(",");
                this.page.setAsc(ascs);
            }else{
                this.page.setAsc(new String[]{asc});
            }
            if(asc.indexOf(",")>-1){
                String[] descs=desc.split(",");
                this.page.setDesc(descs);
            }else{
                this.page.setDesc(new String[]{desc});
            }
        }
    }

    public Page<T> getPage() {
        return page;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public long getPageSize() {
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

    public long getCurrPage() {
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
