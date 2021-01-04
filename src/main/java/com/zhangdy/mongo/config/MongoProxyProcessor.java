package com.zhangdy.mongo.config;


import com.zhangdy.mongo.config.annotation.NonNeedFreshProxy;
import com.zhangdy.mongo.config.mongo.MongoProxy;
import com.zhangdy.mongo.repository.MongoRepository;
import com.zhangdy.mongo.repository.impl.MongoRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@Slf4j
public class MongoProxyProcessor implements BeanPostProcessor {
    @Autowired
    private ApplicationContext applicationContext;

    private static final String MONGO_REPOSITORY_SIMPLE_NAME = MongoRepository.class.getSimpleName();
    private static final int ZERO = 0;
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields=bean.getClass().getDeclaredFields();
        for(Field field : fields){
            boolean isMongoRepository = field.getType().getSimpleName().equalsIgnoreCase(MONGO_REPOSITORY_SIMPLE_NAME);
            if (isMongoRepository) {
                NonNeedFreshProxy annotation = field.getAnnotation(NonNeedFreshProxy.class);
                if (annotation != null) {
                    continue;
                }
                try {
                    field.setAccessible(Boolean.TRUE);
                    ResolvableType type = ResolvableType.forField(field).getGeneric(ZERO);
                    Class<?> clazz = type.resolve();
                    Object impl = field.get(bean);
                    if (impl == null) {
                        impl = applicationContext.getBean(MongoRepositoryImpl.class);
                    }
                    Object proxy =  MongoProxy.createProxy(field.getType().getClassLoader(),
                            new Class[]{field.getType()}, impl, clazz);
                    field.set(bean, proxy);
                } catch (Exception e) {
                    log.error("bean注入异常 ", e);
                }
            }
        }
        return bean;
    }

}