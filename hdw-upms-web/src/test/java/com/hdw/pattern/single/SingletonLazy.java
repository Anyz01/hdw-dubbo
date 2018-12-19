package com.hdw.pattern.single;

/**
 * @Description 单例模式 懒汉式
 * @Author TuMinglong
 * @Date 2018/10/23 11:16
 */
public class SingletonLazy {
    //静态内部类，既实现了线程安全，又避免了同步带来的性能影响
    private static class LazyHolder {
        private static final SingletonLazy INSTANCE = new SingletonLazy();
    }

    private SingletonLazy() {
    }

    public static final SingletonLazy getInstance() {
        return LazyHolder.INSTANCE;
    }
}
