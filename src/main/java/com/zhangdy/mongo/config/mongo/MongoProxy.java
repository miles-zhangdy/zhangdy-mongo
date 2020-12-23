package com.zhangdy.mongo.config.mongo;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MongoProxy implements InvocationHandler {

    private Object object;
    private Class<?> clazz;

    private static Map<Class<?>, Object> MAP = new HashMap<>();

    public MongoProxy(Object object, Class<?> clazz){
        this.object = object;
        this.clazz = clazz;
    }

    public static Object createProxy(ClassLoader loader, Class<?>[] interfaces, Object object, Class<?> clazz){
        if (MAP.containsKey(clazz)) {
            log.info("{} 代理已经创建", clazz.getSimpleName());
            return MAP.get(clazz);
        }
        Object proxy = Proxy.newProxyInstance(loader, interfaces, new MongoProxy(object, clazz));
        MAP.put(clazz, proxy);
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MongoThreadLocal.setClazz(clazz);
        try {
            return method.invoke(object, args);
        }finally {
            MongoThreadLocal.removeClazz();
        }
    }


}