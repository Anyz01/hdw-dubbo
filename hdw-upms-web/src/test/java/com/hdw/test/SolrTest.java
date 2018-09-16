package com.hdw.test;


import com.hdw.solr.config.ISolrService;
import com.hdw.solr.config.SolrObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrTest {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISolrService solrService;

    @Test
    public void createIndex() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> par = new HashMap<>();
        /**
         * 添加字段索引，id一样，为修改，id不一样，为新增
         */
        par.put("id", "1");
        /**
         * 商品类目
         */
        par.put("item_cat_name", "电脑办公类");
        /**
         * 商品名称
         */
        par.put("item_name", "联想天逸510S");
        /**
         * 商品简单描述
         */
        par.put("item_desc", "联想（Lenovo）天逸510S商用台式办公电脑整机（i3-7100 4G 1T 集显 WiFi 蓝牙 三年上门 win10）19.5英寸");
        /**
         * 商品价格
         */
        par.put("item_price", "3099.00");
        /**
         * 将创建好的单个SolrInputDocument对象添加进Solr文档集合列表中
         */
        list.add(par);

        Map<String, Object> par2 = new HashMap<>();
        par2.put("id", "2");
        par2.put("item_cat_name", "电脑办公类");
        par2.put("item_name", "联想天逸510S");
        par2.put("item_desc", "联想（Lenovo）天逸510S商用台式办公电脑整机（i3-7100 4G1T 集显 WiFi 蓝牙 三年上门 win10）21.5英寸");
        par2.put("item_price", "3299.00");
        list.add(par2);

        Map<String, Object> par3 = new HashMap<>();
        par3.put("id", "3");
        par3.put("item_cat_name", "电脑办公类");
        par3.put("item_name", "MacBook Pro");
        par3.put("item_desc", "APPLE 苹果MacBook Pro 17年新款 笔记本电脑高端轻薄时尚游戏办公 15.4英寸 Bar i7 16G 256G闪存 2016款 银色");
        par3.put("item_price", "13838.00");
        list.add(par3);

        Map<String, Object> par4 = new HashMap<>();
        par4.put("id", "4");
        par4.put("item_cat_name", "电脑办公类");
        par4.put("item_name", "华为MateBook X");
        par4.put("item_desc", "华为(HUAWEI) MateBook X 13英寸超轻薄微边框笔记本(i5-7200U 8G 256G 拓展坞 2K屏 指纹 office)玫瑰金");
        par4.put("item_price", "7688.00");
        list.add(par4);

        Map<String, Object> par5 = new HashMap<>();
        par5.put("id", "5");
        par5.put("item_cat_name", "电脑办公类");
        par5.put("item_name", "苹果（Apple） MacBook PRO");
        par5.put("item_desc", "苹果（Apple） MacBook PRO苹果笔记本电脑 2016/2017新款原装日版 17款15英寸灰MPTT2Bar/16G/512G");
        par5.put("item_price", "17688");
        list.add(par5);
        solrService.createIndex(list);
    }

    @Test
    public void queryList() {
        List<SolrObject> queryList = new ArrayList<>();
        SolrObject solrObject = new SolrObject();
        solrObject.setName("item_cat_name");
        solrObject.setQuery("电脑办公类");
        solrObject.setIsQuery(true);
        solrObject.setIsSort("n");
        solrObject.setQueryType("n");
        queryList.add(solrObject);

        SolrObject solrObject2 = new SolrObject();
        solrObject2.setName("item_price");
        solrObject2.setQuery("[10000 TO 150000]");
        solrObject2.setIsQuery(true);
        solrObject2.setIsSort("n");
        solrObject2.setQueryType("n");
        queryList.add(solrObject2);

        List<Map<String, Object>> list = solrService.queryList(true, queryList);
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                for (String key : map.keySet()) {
                    System.out.println("key:" + key + " value:" + map.get(key));
                }
                System.out.println("----我是分割线----");
            }
        }

        List<Map<String, Object>> list2 = solrService.queryList(0, 5, true, queryList);
        if (list2 != null && list2.size() > 0) {
            for (Map<String, Object> map : list2) {
                for (String key : map.keySet()) {
                    System.out.println("key:" + key + " value:" + map.get(key));
                }
                System.out.println("----我是分割线----");
            }
        }

    }

}
