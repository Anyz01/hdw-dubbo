package com.hdw.pattern.single;

import java.util.concurrent.Callable;

/**
 * @Description 单例模式 饿汉模式 线程
 * @Author TuMinglong
 * @Date 2018/10/23 11:57
 */
public class SingletonHungryCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println(SingletonHungry.getInstance());
        return "单例模式 饿汉模式";
    }
}
