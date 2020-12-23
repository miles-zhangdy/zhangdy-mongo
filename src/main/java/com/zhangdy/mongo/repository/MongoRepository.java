package com.zhangdy.mongo.repository;

import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;

public interface MongoRepository<T> {

    /**
     * 保存一个对象
     * @param t
     */
    void save(T t);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
     T getById(Serializable id);

    /**
     * 条件查询返回列表
     * @param t
     * @return
     */
     List findList(T t);

    /**
     * 条件查询返回列表
     * @param query
     * @return
     */
    List findList(Query query);

    /**
     * 条件查询返回单个对象
     * @param t
     * @return
     */
     T getOne(T t);

    /**
     * 条件删除
     * @param t
     * @return
     */
     int remove(T t);

    /**
     * 根据id删除
     * @param id
     * @return
     */
     int removeById(Serializable id);

    /**
     * 修改匹配到的第一个
     * @param sourceT
     * @param targetT
     */
     void modifyFirst(T sourceT, T targetT);

    /**
     * 修改多个
     * @param sourceT
     * @param targetT
     */
     void modifyMulti(T sourceT, T targetT);

    /**
     * 存在则修改 不存在则保存
     * @param sourceT
     * @param targetT
     */
     void saveOrModify(T sourceT, T targetT);

}
