package com.hdw.solr.config;

import com.hdw.common.util.BeanUtils;
import com.hdw.common.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.ValidatingJsonMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * @author TuMinglong
 * @Description com.hdw.solr.config
 * @date 2018/8/16 22:05
 */
@Component
public class SolrServiceImpl implements ISolrService {

    private static Logger logger = LoggerFactory.getLogger(SolrServiceImpl.class);
    @Autowired
    private SolrClient client;

    private String SimplePre = "<font color='red'>";
    private String SimplePost = "</font>";

    @Override
    public void createIndex(List<Map<String, Object>> list) {
        try {
            List<SolrInputDocument> docList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                //建立存储对象
                SolrInputDocument doc = new SolrInputDocument();
                //遍历map
                Iterator<?> entries = list.get(i).entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    String key = entry.getKey().toString();
                    String value = entry.getValue().toString();
                    if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                        doc.addField(key, value);
                    }
                }
                docList.add(doc);
            }
            client.add(docList);
            client.commit();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public void deleteByQuery(String query) {
        try {
            client.deleteByQuery(query);
            client.commit(false, false);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteAll() {
        try {
            client.deleteByQuery("*:*");
            client.commit(false, false);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Map<String, Object>> queryList(boolean highlight, List<SolrObject> list) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        QueryResponse response = null;
        try {
            //创建solr查询对象
            SolrQuery query = new SolrQuery();
            //拼接查询sql
            StringBuilder sb = new StringBuilder();
            //开启高亮功能&高亮样式
            if (highlight) {
                query.setHighlight(true);
                query.setHighlightSimplePre(SimplePre);
                query.setHighlightSimplePost(SimplePost);
            }
            if (list != null && list.size() > 0) {
                for (SolrObject solrObject : list) {
                    //isQuery=y 则查询、反之不查
                    if (solrObject.getIsQuery()) {
                        //高亮字段
                        query.addHighlightField(solrObject.getName());
                        sb.append(solrObject.getName()).append(":");
                        //queryType 检索运算符 n代表默认查询 反之 使用设置的检索运算符
                        String queryType = solrObject.getQueryType();
                        if (queryType.equals("n")) {
                            sb.append(solrObject.getQuery());
                        } else {
                            sb.append(solrObject.getQuery() + queryType);
                        }
                        sb.append(" OR ");
                    }

                    //isSort 排序字段 n 代表不排序  yd 排序并且是desc 、ya 排序并且是asc
                    if (!solrObject.getIsSort().equals("n")) {
                        if (solrObject.getIsSort().equals("yd")) {
                            query.setSort(solrObject.getIsSort(), SolrQuery.ORDER.desc);
                        } else if (solrObject.getIsSort().equals("ya")) {
                            query.setSort(solrObject.getIsSort(), SolrQuery.ORDER.asc);
                        }
                    }
                }
            }

            //减去拼接后三位
            String querys = sb.toString();
            if (querys.length() > 0) {
                querys = querys.substring(0, querys.length() - 3);
            }
            query.set("start", 0);
            query.set("rows", Integer.MAX_VALUE);

            //打印查询sql
            System.out.println("solr query key :" + querys);
            logger.info("solr query key :" + querys);

            //放入查询对象
            query.setQuery(querys);

            //返回结果集
            response = client.query(query);

            // 文档结果集
            SolrDocumentList docs = response.getResults();

            for (SolrDocument doc : docs) {
                Map<String, Object> valMap = doc.getFieldValueMap();
                resultList.add(valMap);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> queryList(Integer pageNum, Integer pageSize, boolean highlight, List<SolrObject> list) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        QueryResponse response = null;
        try {
            //创建solr查询对象
            SolrQuery query = new SolrQuery();
            //拼接查询sql
            StringBuilder sb = new StringBuilder();
            //开启高亮功能&高亮样式
            if (highlight) {
                query.setHighlight(true);
                query.setHighlightSimplePre(SimplePre);
                query.setHighlightSimplePost(SimplePost);
            }
            if (list != null && list.size() > 0) {
                for (SolrObject solrObject : list) {
                    //isQuery=y 则查询、反之不查
                    if (solrObject.getIsQuery()) {
                        //高亮字段
                        query.addHighlightField(solrObject.getName());
                        sb.append(solrObject.getName()).append(":");
                        //queryType 检索运算符 n代表默认查询 反之 使用设置的检索运算符
                        String queryType = solrObject.getQueryType();
                        if (queryType.equals("n")) {
                            sb.append(solrObject.getQuery());
                        } else {
                            sb.append(solrObject.getQuery() + queryType);
                        }
                        sb.append(" OR ");
                    }

                    //isSort 排序字段 n 代表不排序  yd 排序并且是desc 、ya 排序并且是asc
                    if (!solrObject.getIsSort().equals("n")) {
                        if (solrObject.getIsSort().equals("yd")) {
                            query.setSort(solrObject.getIsSort(), SolrQuery.ORDER.desc);
                        } else if (solrObject.getIsSort().equals("ya")) {
                            query.setSort(solrObject.getIsSort(), SolrQuery.ORDER.asc);
                        }
                    }
                }
            }

            //减去拼接后三位
            String querys = sb.toString();
            if (querys.length() > 0) {
                querys = querys.substring(0, querys.length() - 3);
            }

            //分页规则 不等于空 分页  等于空   不分页
            if (pageSize != null && pageNum != null) {
                query.setStart(pageNum * pageSize);//起始页数
                query.setRows(pageSize);//每页总条数
            } else {
                query.set("start", 0);
                query.set("rows", Integer.MAX_VALUE);
            }

            //打印查询sql
            System.out.println("solr query key :" + querys);
            logger.info("solr query key :" + querys);

            //放入查询对象
            query.setQuery(querys);

            // 执行查询
            response = client.query(query);
            // 文档结果集
            SolrDocumentList docs = response.getResults();
            // 高亮显示的返回结果
            Map<String, Map<String, List<String>>> maplist = response.getHighlighting();

            for (SolrDocument doc : docs) {
                Map<String, Object> valMap = doc.getFieldValueMap();
                resultList.add(valMap);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
