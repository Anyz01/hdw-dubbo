package com.hdw.upms.controller;

import com.hdw.common.base.BaseController;
import com.hdw.task.config.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
	private  AsyncTask task;

    @GetMapping(value = "/task")
    @ResponseBody
    public Object task() {
        try{
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
            logger.info("任务全部完成，总耗时：" + (end - start) + "毫秒");
            return renderSuccess(Long.toString(end-start));
        }catch (Exception e){
            return renderError("运行错误，请联系管理员");
        }
    }
}
