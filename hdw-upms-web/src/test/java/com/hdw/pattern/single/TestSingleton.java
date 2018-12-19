package com.hdw.pattern.single;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

/**
 * @Description 单例测试
 * @Author TuMinglong
 * @Date 2018/10/23 11:41
 */
@RunWith(SpringRunner.class)
public class TestSingleton {
    /**
     * 单例类只能有一个实例
     * 单例类必须自己创建自己的唯一实例
     * 单例类必须给所有其他对象提供这一实例
     * 从速度和反应时间角度来讲，非延迟加载（饿汉模式）好，从资源和利用效率上说，延迟加载（懒汉模式）好
     */

    @Test
    public void singletonLazy() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));
        SingletonLazyCallable sc = new SingletonLazyCallable();
        SingletonLazyCallable sc2 = new SingletonLazyCallable();
        SingletonLazyCallable sc3 = new SingletonLazyCallable();
        FutureTask<String> futureTask = new FutureTask<>(sc);
        FutureTask<String> futureTask2 = new FutureTask<>(sc2);
        FutureTask<String> futureTask3 = new FutureTask<>(sc3);
        executor.submit(futureTask);
        executor.submit(futureTask2);
        executor.submit(futureTask3);
        executor.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程在执行任务");

        try {
            System.out.println("sc运行结果：" + futureTask.get());
            System.out.println("sc2运行结果：" + futureTask2.get());
            System.out.println("sc3运行结果：" + futureTask3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("所有任务执行完毕");

    }

    @Test
    public void singletonLazy2() {
        SingletonLazy2 singletonLazy2 = SingletonLazy2.getInstance();
        singletonLazy2.setName("测试");
        SingletonLazy2 singletonLazy22 = SingletonLazy2.getInstance();
        singletonLazy22.setName("测试2");

        singletonLazy2.printInfo();
        singletonLazy22.printInfo();

        if (singletonLazy2 == singletonLazy22) {
            System.out.println("创建的是同一个实例");
        } else {
            System.out.println("创建的不是同一个实例");
        }

    }

    @Test
    public void singletonHungry() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));
        SingletonHungryCallable sc = new SingletonHungryCallable();
        SingletonHungryCallable sc2 = new SingletonHungryCallable();
        SingletonHungryCallable sc3 = new SingletonHungryCallable();
        FutureTask<String> futureTask = new FutureTask<>(sc);
        FutureTask<String> futureTask2 = new FutureTask<>(sc2);
        FutureTask<String> futureTask3 = new FutureTask<>(sc3);
        executor.submit(futureTask);
        executor.submit(futureTask2);
        executor.submit(futureTask3);
        executor.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程在执行任务");

        try {
            System.out.println("sc运行结果：" + futureTask.get());
            System.out.println("sc2运行结果：" + futureTask2.get());
            System.out.println("sc3运行结果：" + futureTask3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("所有任务执行完毕");

    }
}
