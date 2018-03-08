package com.hdw.dubbo.search.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hdw.dubbo.common.config.redis.RedisUtil;
import com.hdw.dubbo.search.entity.PushDataIntoSolrRequest;
import com.hdw.dubbo.search.rpc.api.ISearchApiService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected MockMvc mockMvc;
	protected MockHttpServletRequest request;
	protected MockHttpServletResponse response;

	@Autowired
	protected WebApplicationContext wac;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
				.alwaysDo(MockMvcResultHandlers.print())
				.build();
		request = new MockHttpServletRequest();
		request.setCharacterEncoding("UTF-8");
		response = new MockHttpServletResponse();
	}

	@Autowired
	private RedisUtil cacheManager;
	@Autowired
	private ISearchApiService searchApiService;

	@Test
	public void insertSolr() {
		List<Map<String,Object>> docs = new ArrayList<>();

		Map<String,Object> doc = new HashMap<>();
		/**
		 * 添加字段索引，id一样，为修改，id不一样，为新增
		 */
		doc.put("id", "1");
		/**
		 * 商品类目
		 */
		doc.put("item_cat_name", "电脑办公类");
		/**
		 * 商品名称
		 */
		doc.put("item_name", "联想天逸510S");
		/**
		 * 商品简单描述
		 */
		doc.put("item_desc", "联想（Lenovo）天逸510S商用台式办公电脑整机（i3-7100 4G 1T 集显 WiFi 蓝牙 三年上门 win10）19.5英寸");
		/**
		 * 商品价格
		 */
		doc.put("item_price", "3099.00");
		/**
		 * 将创建好的单个SolrInputDocument对象添加进Solr文档集合列表中
		 */
		docs.add(doc);

		doc = new HashMap<>();
		doc.put("id", "2");
		doc.put("item_cat_name", "电脑办公类");
		doc.put("item_name", "联想天逸510S");
		doc.put("item_desc", "联想（Lenovo）天逸510S商用台式办公电脑整机（i3-7100 4G1T 集显 WiFi 蓝牙 三年上门 win10）21.5英寸");
		doc.put("item_price", "3299.00");
		docs.add(doc);

		doc = new HashMap<>();
		doc.put("id", "3");
		doc.put("item_cat_name", "电脑办公类");
		doc.put("item_name", "MacBook Pro");
		doc.put("item_desc", "APPLE 苹果MacBook Pro 17年新款 笔记本电脑高端轻薄时尚游戏办公 15.4英寸 Bar i7 16G 256G闪存 2016款 银色");
		doc.put("item_price", "13838.00");
		docs.add(doc);

		doc = new HashMap<>();
		doc.put("id", "4");
		doc.put("item_cat_name", "电脑办公类");
		doc.put("item_name", "华为MateBook X");
		doc.put("item_desc", "华为(HUAWEI) MateBook X 13英寸超轻薄微边框笔记本(i5-7200U 8G 256G 拓展坞 2K屏 指纹 office)玫瑰金");
		doc.put("item_price", "7688.00");
		docs.add(doc);

		doc = new HashMap<>();
		doc.put("id", "5");
		doc.put("item_cat_name", "电脑办公类");
		doc.put("item_name", "苹果（Apple） MacBook PRO");
		doc.put("item_desc", "苹果（Apple） MacBook PRO苹果笔记本电脑 2016/2017新款原装日版 17款15英寸灰MPTT2Bar/16G/512G");
		doc.put("item_price", "17688");
		docs.add(doc);
		
		
		for(int i=0;i<20000;i++) {
			doc = new HashMap<>();
			doc.put("id", i+5);
			doc.put("item_cat_name", "电脑办公类");
			doc.put("item_name", "苹果（Apple） MacBook PRO");
			doc.put("item_desc", "苹果（Apple） MacBook PRO苹果笔记本电脑 2016/2017新款原装日版 17款15英寸灰MPTT2Bar/16G/512G");
			doc.put("item_price", "17688");
			docs.add(doc);
		}
		
		PushDataIntoSolrRequest request=new PushDataIntoSolrRequest();
		request.setCoreName("realdata");
		request.setList(docs);
        
		searchApiService.insertBatchOfSolr(request);

	}

	@Test
	public void pushDataIntoSolr() { // 推送solr测试方法
		Map<String, Object> map = new HashMap<>();
		map.put("id", "2");
		map.put("item_cat_name", "电脑办公类");
		map.put("item_name", "联想天逸510S");
		map.put("item_desc", "联想（Lenovo）天逸510S商用台式办公电脑整机（i3-7100 4G1T 集显 WiFi 蓝牙 三年上门 win10）21.5英寸");
		map.put("item_price", "3299.00");
		PushDataIntoSolrRequest request = new PushDataIntoSolrRequest();
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		request.setDoc(map);
		request.setCoreName("realdata");
		MvcResult result;
		try {
			java.lang.String requestJson = ow.writeValueAsString(request);
			result = mockMvc
					.perform(post("/solr/add").contentType(MediaType.APPLICATION_JSON_VALUE)
							.content(requestJson))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andReturn();
			System.out.println(result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查询solr方法
	@Test
	public void querySolrIndex() {
		MvcResult result;
		try {
			result = mockMvc
					.perform(post("/solr/dataGrid?page=0&rows=5&keyWord=Mac")
							.contentType(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
					.andReturn();
			System.out.println(result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRedis() {
		System.out.println("------------String测试-----------");
		String phone = "18627026982";

		boolean StringFlag = cacheManager.exists("test");
		if (StringFlag) {
			cacheManager.del("test");
		}

		cacheManager.set("test", phone, 600);
		String phoneS = (String) cacheManager.get("test");
		logger.info("从redis中获取String：" + phoneS);
		cacheManager.del("test");

		System.out.println("------------Map测试-----------");
		boolean MapFlag = cacheManager.exists("testMap");
		if (MapFlag) {
			cacheManager.del("testMap");
		}

		Map<String, Object> par = new HashMap<String, Object>();
		par.put("district", "华容区");
		par.put("businessType", "非煤矿山");
		cacheManager.madd("testMap", par, 600);
		String obj = (String) cacheManager.mget("testMap", "district");
		logger.info("从redis中获取map：key='district' value:" + obj);
		Map<String, Object> par3 = cacheManager.mget("testMap");
		for (String key : par3.keySet()) {
			logger.info("从redis中获取map：key=" + key + " value:" + par3.get(key));
		}
		boolean delMapFlag = cacheManager.mdel("testMap", "district");
		if (delMapFlag) {
			logger.info("从redis中删除map中key为district的对象成功");
		}

		System.out.println("------------Set测试-----------");
		boolean SetFlag = cacheManager.exists("testSet");
		if (SetFlag) {
			cacheManager.del("testSet");
		}
		cacheManager.sadd("testSet", "涂明龙");
		cacheManager.sadd("testSet", "陈实");
		cacheManager.sadd("testSet", "汪聪");
		cacheManager.sadd("testSet", "谭美雄");
		cacheManager.sadd("testSet", "朱贝贝");
		Set<Serializable> set = cacheManager.sget("testSet");

		for (Serializable s : set) {
			logger.info("从redis中获取set：" + s.toString());
		}
		boolean delSetFlag = cacheManager.sdel("testSet", "涂明龙");
		if (delSetFlag) {
			logger.info("从redis中删除set中为涂明龙的对象成功");
		}

		System.out.println("------------List测试-----------");
		boolean ListFlag = cacheManager.exists("testList");
		if (ListFlag) {
			cacheManager.del("testList");
		}
		List<String> list = new ArrayList<String>();
		list.add("涂明龙");
		list.add("陈实");
		list.add("汪聪");
		list.add("谭美雄");
		list.add("朱贝贝");
		cacheManager.ladd("testList", list, 600);

		List<String> list2 = cacheManager.lget("testList");
		for (String s : list2) {
			logger.info("从redis中获取List：" + s);
		}
	}

}
