package com.hdw.thread;

import java.util.concurrent.*;

/**
 * @Description com.hdw.thread
 * @Date 2018/9/5 9:51
 * @Author TuMingling
 */
public class FutureTaskTest {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        CallableDemo c = new CallableDemo();
//        Future<Integer> futureTask = executorService.submit(c);

        FutureTask<Integer> futureTask = new FutureTask<Integer>(c);
        executorService.submit(futureTask);


        executorService.shutdown();


        Thread.sleep(1000);

        System.out.println("主线程在执行任务");

        try {
            System.out.println("task运行结果" + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }
}

class CallableDemo implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("子线程在进行计算");
        Thread.sleep(3000);
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        return sum;
    }
}
