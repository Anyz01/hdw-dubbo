package com.hdw.upms.controller;

import com.hdw.common.base.BaseController;
import com.hdw.common.result.Result;
import com.hdw.upms.task.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Future;

/**
 * 
 * @Descriptin 测试控制器
 * @author TuMinglong
 * @Date 2018年4月27日 下午2:59:17
 */

@Controller
@RequestMapping("/test")
public class TestController extends BaseController {
	
	private final AsyncTask task;

    @Autowired
    public TestController(AsyncTask task) {
        this.task = task;
    }

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public Result task() throws Exception {
        long start = System.currentTimeMillis();

        Future<String> task1 = task.doTaskOne();
        Future<String> task2 = task.doTaskTwo();
        Future<String> task3 = task.doTaskThree();

        while (true) {
            if (task1.isDone() && task2.isDone() && task3.isDone()) {
                // 三个任务都调用完成，退出循环等待
                break;
            }
            Thread.sleep(1000);
        }

        long end = System.currentTimeMillis();

        System.out.println("任务全部完成，总耗时：" + (end - start) + "毫秒");
        return new Result(200, true, Long.toString(end-start));
    }
    
    
    @RequestMapping("/hello")
    @ResponseBody
    public Object hello() throws RuntimeException {
        throw new RuntimeException("发生错误");
    }


   
}
