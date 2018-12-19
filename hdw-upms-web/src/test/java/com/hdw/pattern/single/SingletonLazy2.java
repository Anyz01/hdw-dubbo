package com.hdw.pattern.single;

/**
 * @Description 单例模式 懒汉式2
 * @Author TuMinglong
 * @Date 2018/10/23 13:57
 */
public class SingletonLazy2 {

    String name;

    private SingletonLazy2() {
    }

    private static volatile SingletonLazy2 instance = null;

    public static SingletonLazy2 getInstance() {
        if (instance == null) {
            synchronized (SingletonLazy2.class) {
                if (instance == null) {
                    instance = new SingletonLazy2();
                }
            }
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void printInfo() {
        System.out.println("this name is " + name);
    }
}
