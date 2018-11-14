package com.hdw.thread;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

/**
 * @Description com.hdw.thread
 * @Date 2018/9/5 10:37
 * @Author TuMingling
 */
@RunWith(SpringRunner.class)
public class JoinTest {

    @Test
    public void test() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        JoinThread joinThread = new JoinThread("测试");

        FutureTask<Integer> futureTask = new FutureTask<>(joinThread);
        Thread thread = new Thread(futureTask);
        thread.start();

        for (int i = 0; i < 100; i++) {
            if (i == 20) {
                JoinThread joinThread2 = new JoinThread("被阻塞");
                FutureTask<Integer> futureTask2 = new FutureTask<>(joinThread2);
                Thread thread2 = new Thread(futureTask2);
                thread2.start();
                //main线程调用thread2线程的join()方法，main线程必须等thread2执行结束才会执行
                try {
                    thread2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println("thread2运行结果" + futureTask.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " " + i);
        }

        System.out.println("主线程在执行任务");

        try {
            System.out.println("thread运行结果" + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");

    }
}

class JoinThread implements Callable<Integer> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JoinThread(String name) {
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
