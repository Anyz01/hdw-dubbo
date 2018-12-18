package com.hdw.common.result;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hdw.common.util.JacksonUtils;
import com.hdw.common.xss.SQLFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @Description PageInfo 分页实体类 (结合EasyUI、Jquery Table) 适用于mybatis-plus 3.0版本
 * @Author TuMinglong
 * @Date 2018/12/10 14:41
 */
public class PageInfo<T> implements Serializable {

    private long total; // 总记录 
    private List rows; //显示的记录

    @JsonIgnore
    private int from;
    @JsonIgnore
    private int size;
    /**
     * mybatis-plus分页参数
     */
    @JsonIgnore
    private Page<T> page;

    public PageInfo() {
    }

    // 构造方法
    public PageInfo(int nowPage, int pageSize, String asc, String desc) {
        //计算当前页
        if (nowPage < 0) {
            nowPage = 1;
        } else {
            //当前页
            nowPage = nowPage / pageSize + 1;
        }
        //记录每页显示的记录数
        if (pageSize < 0) {
            pageSize = 10;
        }
        //计算开始的记录和结束的记录
        this.from = (nowPage - 1) * pageSize;
        this.size = pageSize;

        //mybatis-plus分页
        this.page = new Page<>(nowPage, pageSize);

        //排序
        if (StringUtils.isNotBlank(asc) && StringUtils.isNotBlank(desc)) {
            //防止SQL注入（因为asc、order是通过拼接SQL实现排序的，会有SQL注入风险）
            String ascs= SQLFilter.sqlInject(asc);
            String descs = SQLFilter.sqlInject(desc);
            if(ascs.indexOf(",")>-1){
                String[] ascArr=ascs.split(",");
                this.page.setAsc(ascArr);
            }else{
                this.page.setAsc(new String[]{ascs});
            }
            if(descs.indexOf(",")>-1){
                String[] descArr=descs.split(",");
                this.page.setDesc(descArr);
            }else{
                this.page.setDesc(new String[]{descs});
            }
        }
    }

    //构造方法
    public PageInfo(int nowpage, int pagesize) {
        this(nowpage, pagesize, "", "");
    }

    public PageInfo(IPage<T> iPage) {
        this.total = iPage.getTotal();
        this.rows =iPage.getRecords();
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
