package com.hdw.thread;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Description
 * @Date 2018/9/5 11:18
 * @Author TuMingling
 */
@RunWith(SpringRunner.class)
public class YieldTest {

    @Test
    public void test() {
        YieldThread yieldThread = new YieldThread("让步线程1");
        FutureTask<Integer> futureTask = new FutureTask<>(yieldThread);
        Thread thread = new Thread(futureTask);
        thread.setPriority(10);
        thread.start();

        YieldThread yieldThread2 = new YieldThread("让步线程2");
        FutureTask<Integer> futureTask2 = new FutureTask<>(yieldThread2);
        Thread thread2 = new Thread(futureTask2);
        thread2.setPriority(5);
        thread2.start();

        System.out.println("主线程在执行任务");

        try {
            System.out.println("thread运行结果" + futureTask.get());
            System.out.println("thread2运行结果" + futureTask2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");


    }


}

class YieldThread implements Callable<Integer> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YieldThread(String name) {
        this.name = name;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            System.out.println(getName() + " " + i);
            if (i == 50) {
                sum = i;
                Thread.yield();
            }
        }
        return sum;
    }
}
