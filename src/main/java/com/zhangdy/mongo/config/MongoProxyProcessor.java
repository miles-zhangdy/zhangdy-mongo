package com.zhangdy.mongo.config;


import com.zhangdy.mongo.config.mongo.MongoProxy;
import com.zhangdy.mongo.repository.MongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@Slf4j
public class MongoProxyProcessor implements BeanPostProcessor {

    private static final String MONGO_REPOSITORY_SIMPLE_NAME = MongoRepository.class.getSimpleName();
    private static final int ZERO = 0;
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields=bean.getClass().getDeclaredFields();
        for(Field field : fields){
            boolean isMongoRepository = field.getType().getSimpleName().equalsIgnoreCase(MONGO_REPOSITORY_SIMPLE_NAME);
            if (isMongoRepository) {
                try {
                    field.setAccessible(Boolean.TRUE);
                    ResolvableType type = ResolvableType.forField(field).getGeneric(ZERO);
                    Class<?> clazz = type.resolve();
                    Object proxy =  MongoProxy.createProxy(field.getType().getClassLoader(),
                            new Class[]{field.getType()}, field.get(bean), clazz);
                    field.set(bean, proxy);
                } catch (Exception e) {
                    log.error("bean注入异常 ", e);
                }
            }
        }
        return bean;
    }

}