package com.zhangdy.mongo.repository.impl;

import com.zhangdy.mongo.config.mongo.MongoThreadLocal;
import com.zhangdy.mongo.repository.MongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;


@Repository
public  class MongoRepositoryImpl<T> implements MongoRepository<T> {

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public  void save(T t) {
        mongoTemplate.save(t);
    }

    @Override
    public T getById(Serializable id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return this.mongoTemplate.findOne(query, getClazz());
    }

    @Override
    public List findList(T t) {
        Query query = getQueryByObject(t);
        return mongoTemplate.find(query, getClazz());
    }

    @Override
    public List findList(Query query) {
        return mongoTemplate.find(query, this.getClazz());
    }

    @Override
    public T getOne(T t) {
        Query query = getQueryByObject(t);
        return mongoTemplate.findOne(query, getClazz());
    }

    @Override
    public int remove(T t) {
        return (int) this.mongoTemplate.remove(t).getDeletedCount();
    }

    @Override
    public int removeById(Serializable id) {
        Criteria criteria = Criteria.where("_id").is(id);
        Query query = new Query(criteria);
        T obj = this.mongoTemplate.findOne(query, getClazz());
        if (obj != null) {
            return  (int) this.mongoTemplate.remove(obj).getDeletedCount();
        }
        return 0;
    }

    @Override
    public void modifyFirst(T sourceT, T targetT) {
        Query query = getQueryByObject(sourceT);
        Update update = getUpdateByObject(targetT);
        this.mongoTemplate.updateFirst(query, update, MongoThreadLocal.getClazz());
    }

    @Override
    public void modifyMulti(T sourceT, T targetT) {
        Query query = getQueryByObject(sourceT);
        Update update = getUpdateByObject(targetT);
        this.mongoTemplate.updateMulti(query, update, getClazz());
    }

    @Override
    public void saveOrModify(T sourceT, T targetT) {
        Query query = getQueryByObject(sourceT);
        Update update = getUpdateByObject(targetT);
        this.mongoTemplate.upsert(query, update, getClazz());
    }

    private Class<T> getClazz(){
        return (Class<T>) MongoThreadLocal.getClazz();
    }

    /**
     * 将查询条件对象转换为query
     *
     * @param object
     * @return
     * @author Jason
     */
    public Query getQueryByObject(T object) {
        Query query = new Query();

        Field[] declaredFields = object.getClass().getDeclaredFields();
        Criteria criteria = new Criteria();
        try {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(Boolean.TRUE);
                if(declaredField.get(object) != null){
                    criteria.and(declaredField.getName()).is(declaredField.get(object));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addCriteria(criteria);
        return query;
    }

    /**
     * 将查询条件对象转换为update
     *
     * @param object
     * @return
     * @author Jason
     */
    public Update getUpdateByObject(T object) {
        Update update = new Update();

        Field[] declaredFields = object.getClass().getDeclaredFields();
        try {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(Boolean.TRUE);
                Object filedValue = declaredField.get(object);
                update.set(declaredField.getName(), filedValue);
            }
        } catch (Exception e) {
        }
        return update;
    }

}
