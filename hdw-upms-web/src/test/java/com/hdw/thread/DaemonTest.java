package com.hdw.thread;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Description com.hdw.thread
 * @Date 2018/9/5 11:08
 * @Author TuMingling
 */
@RunWith(SpringRunner.class)
public class DaemonTest {

    @Test
    public void test() {
        DaemonThread daemonThread = new DaemonThread("守护线程");
        FutureTask<Integer> futureTask = new FutureTask<>(daemonThread);
        Thread thread = new Thread(futureTask);
        thread.setDaemon(true);
        thread.start();
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }

        System.out.println("主线程在执行任务");
        try {
            System.out.println("thread执行结果：" + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");

    }

}


class DaemonThread implements Callable<Integer> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DaemonThread(String name) {
        this.name = name;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            System.out.println(getName() + " " + i);
            sum = i;
        }
        return sum;
    }
}
