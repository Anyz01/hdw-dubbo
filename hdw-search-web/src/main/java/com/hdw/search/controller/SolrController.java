package com.hdw.search.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdw.common.base.BaseController;
import com.hdw.common.result.PageInfo;
import com.hdw.search.entity.PushDataIntoSolrRequest;
import com.hdw.search.api.ISearchApiService;

@Controller
@RequestMapping("/solr")
public class SolrController extends BaseController{
	
	@Reference(version = "1.0.0", application = "${dubbo.application.id}", url = "dubbo://localhost:20881")
	private ISearchApiService searchApiService;
	
	 /** 
     * 添加入solr索引  
     */  
    @PostMapping(value = "/add")
    @ResponseBody
    public Object pushDataIntoSolr(@RequestBody  PushDataIntoSolrRequest request){   
        if (!searchApiService.insertOfSolr(request)){  
        	return renderError(500,"插入失败，请稍候重试。");
        } else {  
        	return renderSuccess("请求成功，数据插入成功。");  
        }  
    }  
    
    
    /**
     * 
     * @param page 页数
     * @param rows 行数
     * @param keyWord 索引字段 值
     * @param keyWord2 索引字段2 值
     * @return
     */
    @PostMapping(value = "/dataGrid")
    @ResponseBody
    public PageInfo dataGrid(Integer page, Integer rows,String keyWord, String keyWord2){  
  
        SolrQuery solrQuery = new SolrQuery(); 
		 /** 
         * *:*:表示查询全部 *Mac*代表左右模糊匹配，如果写Mac代表绝对匹配 
         */ 
	    StringBuffer sb=new StringBuffer();
		if(StringUtils.isNotBlank(keyWord)) {
			sb.append("item_name"+":"+keyWord+"*");
		}
		if(StringUtils.isNotBlank(keyWord2)) {
			sb.append("and");
			sb.append("item_price"+":"+"*"+keyWord2+"*");
		}
        solrQuery.setQuery(sb.toString());  
  
        /** 
         * 过滤条件:电脑办公类，10000元以上 [A TO B] 范围A到B之间 [A TO *] A到无穷 [* TO B] B以下 
         * 相当于sql语句中的where ----->fq = filter query 
         */  
        solrQuery.set("fq", "item_cat_name:电脑办公类");  
        solrQuery.set("fq", "item_price:[10000 TO *]");  
  
        // 分页，0开始，每页5条，setStart设置的就是显示第几页  
        solrQuery.setStart(page);  
        solrQuery.setRows(rows);  
        // 开启高亮  
        solrQuery.setHighlight(true);  
        // 添加高亮字段，多个字段之间逗号隔开比如: A,B,C  
        solrQuery.addHighlightField("item_name,item_price");  
        // 设置高亮字段的前缀  
        solrQuery.setHighlightSimplePre("<font color='red'>");  
        // 设置高亮字段的后缀  
        solrQuery.setHighlightSimplePost("</font>");  
  
        // 执行查询  
        String coreName="realdata";
        QueryResponse response = searchApiService.queryOfSolr(coreName, solrQuery);  
  
        // 文档结果集  
        SolrDocumentList docs = response.getResults();  
  
        System.err.println("-------------------高亮效果部分展示-------------------------");  
        // 高亮显示的返回结果  
        Map<String, Map<String, List<String>>> maplist = response.getHighlighting();  
        /** 
         * 静态html资源里面的对象 -- ${list} 
         */  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
        Map<String, Object> m;  
        // 返回高亮之后的结果..  
        for (SolrDocument solrDocument : docs) {  
            String id = solrDocument.getFirstValue("id").toString();  
            String item_cat_name = solrDocument.getFirstValue("item_cat_name").toString();  
            String item_desc = solrDocument.getFirstValue("item_desc").toString();  
            String item_price = solrDocument.getFirstValue("item_price").toString();  
  
            System.err.println(id);  
            System.err.println(item_cat_name);  
            Map<String, List<String>> fieldMap = maplist.get(solrDocument.get("id"));  
            List<String> stringlist = fieldMap.get("item_name");  
  
            String item_name = stringlist.get(0);  
  
            System.err.println(item_name);  
            System.err.println(item_desc);  
            System.err.println(item_price);  
            m = new HashMap<String, Object>();  
            m.put("id", id);  
            m.put("item_cat_name", item_cat_name);  
            m.put("item_name", item_name);  
            m.put("item_desc", item_desc);  
            m.put("item_price", item_price);  
            list.add(m);  
            System.err.println("============分割线==============");  
        }  
        
        SolrQuery solrQuery2= new SolrQuery(); 
        solrQuery2.setQuery(sb.toString());
        QueryResponse response2 = searchApiService.queryOfSolr(coreName, solrQuery2);  
        SolrDocumentList doc2 = response2.getResults();
        PageInfo pageInfo=new PageInfo(page, rows);
        pageInfo.setRows(list);
        pageInfo.setTotal(doc2.getNumFound());
        System.err.println("查询到的总条数:" + docs.getNumFound() + ", 内容:" + docs);  
        
        return pageInfo;
    }  
    
    
    @GetMapping("/deleteAll")  
    @ResponseBody  
    public Object DeleteAll() throws Exception {  
    	 String coreName="realData";
        // 清空所有数据  
    	boolean flag=searchApiService.DeleteAllOfSolr(coreName);
    	if(flag) {
    		return renderSuccess("删除成功。"); 
    	}else {
    		return renderError(500,"删除失败。"); 
    	} 
    }  

}
