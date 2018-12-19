package com.hdw.pattern.single;

/**
 * @Description 单例模式 饿汉模式
 * @Author TuMinglong
 * @Date 2018/10/23 11:54
 */
public class SingletonHungry {

    private static SingletonHungry singletonHungry = new SingletonHungry();

    public static SingletonHungry getInstance() {
        return singletonHungry;
    }
}
