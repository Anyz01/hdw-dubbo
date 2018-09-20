package com.hdw.thread;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Description com.hdw.thread
 * @Date 2018/9/5 10:29
 * @Author TuMingling
 */
@RunWith(SpringRunner.class)
public class CompletableFutureTest {

    @Test
    public void test(){
        CompletableFuture<String> completableFuture=new CompletableFuture<>();
        new Thread(() -> {
            //模拟执行耗时任务
            System.out.println("task doing...");
            try {
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            //告诉completableFuture任务已经完成
            completableFuture.complete("ok");
        }).start();
        //获取任务结果，如果没有完成会一直阻塞等待
        try {
            String result=completableFuture.get();
            System.out.println("计算结果："+result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
