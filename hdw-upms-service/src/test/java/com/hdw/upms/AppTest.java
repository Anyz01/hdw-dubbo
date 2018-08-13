package com.hdw.upms;


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

import com.hdw.common.config.redis.IRedisService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IRedisService redisService;

    @Test
    public void testRedis() {

        System.out.println("------------String测试-----------");
        String phone = "18627026982" ;

        boolean StringFlag = redisService.exists("test");
        if (StringFlag) {
            redisService.del("test");
        }

        redisService.set("test" , phone, 600);
        String phoneS = (String) redisService.get("test");
        logger.info("从redis中获取String：" + phoneS);
        redisService.del("test");

        System.out.println("------------Map测试-----------");
        boolean MapFlag = redisService.exists("testMap");
        if (MapFlag) {
            redisService.del("testMap");
        }

        Map<String, Object> par = new HashMap<String, Object>();
        par.put("district" , "华容区");
        par.put("businessType" , "非煤矿山");
        redisService.madd("testMap" , par, 600);
        String obj = (String) redisService.mget("testMap" , "district");
        logger.info("从redis中获取map：key='district' value:" + obj);
        Map<String, Object> par3 = redisService.mget("testMap");
        for (String key : par3.keySet()) {
            logger.info("从redis中获取map：key=" + key + " value:" + par3.get(key));
        }
        boolean delMapFlag = redisService.mdel("testMap" , "district");
        if (delMapFlag) {
            logger.info("从redis中删除map中key为district的对象成功");
        }

        System.out.println("------------Set测试-----------");
        boolean SetFlag = redisService.exists("testSet");
        if (SetFlag) {
            redisService.del("testSet");
        }
        redisService.sadd("testSet" , "涂明龙");
        redisService.sadd("testSet" , "陈实");
        redisService.sadd("testSet" , "汪聪");
        redisService.sadd("testSet" , "谭美雄");
        redisService.sadd("testSet" , "朱贝贝");
        Set<Object> set = redisService.sget("testSet");

        for (Object s : set) {
            logger.info("从redis中获取set：" + s.toString());
        }
        boolean delSetFlag = redisService.sdel("testSet" , "涂明龙");
        if (delSetFlag) {
            logger.info("从redis中删除set中为涂明龙的对象成功");
        }

        System.out.println("------------List测试-----------");
        boolean ListFlag = redisService.exists("testList");
        if (ListFlag) {
            redisService.del("testList");
        }
        List<String> list = new ArrayList<String>();
        list.add("涂明龙");
        list.add("陈实");
        list.add("汪聪");
        list.add("谭美雄");
        list.add("朱贝贝");
        redisService.ladd("testList" , list, 600);

        List<String> list2 = redisService.lget("testList");
        for (String s : list2) {
            logger.info("从redis中获取List：" + s);
        }

    }

}
