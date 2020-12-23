package com.zhangdy.mongo.config.mongo;

public class MongoThreadLocal {

    static final ThreadLocal<Class<?>> threadLocal = new ThreadLocal<>();

    public static void setClazz(Class<?> clazz){
        threadLocal.set(clazz);
    }

    public static void removeClazz(){
        threadLocal.remove();
    }

    public static Class<?> getClazz(){
        return threadLocal.get();
    }

}
