package com.zhangdy.mongo.config.annotation;


import java.lang.annotation.*;

/**
 * 加了注解不需要重新 制定 代理对象
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  NonNeedFreshProxy {
}
