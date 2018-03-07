package com.hdw.dubbo.upms;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hdw.dubbo.common.config.redis.RedisUtil;
import com.hdw.dubbo.upms.entity.Company;
import com.hdw.dubbo.upms.rpc.api.ICompanyService;



@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());  
	
	@Autowired
	private ICompanyService companyService;
	
	 @Autowired
	private RedisUtil cacheManager;

	@Test
	public void test(){
		Map<String,Object> columnMap=new HashMap<>();
		List<Company> list=companyService.selectByMap(columnMap);
		System.out.println(list.size());
	}
	    
    @Test  
    public void contextLoads() {  
        logger.trace("I am trace log.");  
        logger.debug("I am debug log.");  
        logger.warn("I am warn log.");  
        logger.error("I am error log.");  
    }  
    
    
   
	
	@Test
	public void testRedis() {
		
		System.out.println("------------String测试-----------");
		String phone="18627026982";
		
		boolean StringFlag=cacheManager.exists("test");
		if(StringFlag) {
			cacheManager.del("test");
		}
		
		cacheManager.set("test", phone, 600);
		String phoneS=(String) cacheManager.get("test");
		logger.info("从redis中获取String："+phoneS);
		cacheManager.del("test");
		
		System.out.println("------------Map测试-----------");
		boolean MapFlag=cacheManager.exists("testMap");
		if(MapFlag) {
			cacheManager.del("testMap");
		}
		
		Map<String,Object> par=new HashMap<String,Object>();
		par.put("district", "华容区");
		par.put("businessType", "非煤矿山");
		cacheManager.madd("testMap", par,600);
		String obj=(String) cacheManager.mget("testMap", "district");
		logger.info("从redis中获取map：key='district' value:"+obj);
		Map<String,Object> par3=cacheManager.mget("testMap");
		for(String key:par3.keySet()) {
			logger.info("从redis中获取map：key="+key+" value:"+par3.get(key));
		}
		boolean delMapFlag=cacheManager.mdel("testMap","district");
		if(delMapFlag) {
			logger.info("从redis中删除map中key为district的对象成功");
		}
		
		System.out.println("------------Set测试-----------");
		boolean SetFlag=cacheManager.exists("testSet");
		if(SetFlag) {
			cacheManager.del("testSet");
		}
		cacheManager.sadd("testSet", "涂明龙");
		cacheManager.sadd("testSet", "陈实");
		cacheManager.sadd("testSet", "汪聪");
		cacheManager.sadd("testSet", "谭美雄");
		cacheManager.sadd("testSet", "朱贝贝");
		Set<Serializable> set=cacheManager.sget("testSet");
		
		for (Serializable s : set) {
			logger.info("从redis中获取set："+s.toString() );
		}
		boolean delSetFlag=cacheManager.sdel("testSet", "涂明龙");
		if(delSetFlag) {
			logger.info("从redis中删除set中为涂明龙的对象成功");
		}
		
		System.out.println("------------List测试-----------");
		boolean ListFlag=cacheManager.exists("testList");
		if(ListFlag) {
			cacheManager.del("testList");
		}
		List<String> list=new ArrayList<String>();
		list.add("涂明龙");
		list.add("陈实");
		list.add("汪聪");
		list.add("谭美雄");
		list.add("朱贝贝");
		cacheManager.ladd("testList", list, 600);
		
		
		List<String> list2=cacheManager.lget("testList");
		for (String s : list2) {
			logger.info("从redis中获取List："+s );
		}
		
	}
	
}
