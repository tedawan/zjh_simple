package com.addplus.server.api.mongodao;


import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.mongodb.morphia.*;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicDao<T, K>  {
    /** @deprecated */
    protected Class<T> entityClazz;
    /** @deprecated */
    protected DatastoreImpl ds;
    public BasicDao(Class<T> entityClass, MongoClient mongoClient, Morphia morphia, String dbName) {
        this.initDS(mongoClient, morphia, dbName);
        this.initType(entityClass);
    }

    public BasicDao(Class<T> entityClass, AdvancedDatastore ds) {
        this.ds = (DatastoreImpl)ds;
        this.initType(entityClass);
    }

    protected BasicDao(MongoClient mongoClient, Morphia morphia, String dbName) {
        this.initDS(mongoClient, morphia, dbName);
        this.initType((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    protected BasicDao(Datastore ds) {
        this.ds = (DatastoreImpl)ds;
        this.initType((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public long count(Class<T> clazz) {
        return ds.getCount(clazz);
    }

    public T get(Class<T> clazz, K id) {
        return ds.get(clazz, id);
    }

    public long count() {

        return this.ds.getCount(this.entityClazz);
    }

    public long count(String key, Object value) {
        return this.count(this.ds.find(this.entityClazz, key, value));
    }

    public long count(Query<T> query) {
        return this.ds.getCount(query);
    }

    public Query<T> createQuery(String collectionName,Class type) {

        return getDs().createQuery(collectionName,type);
    }

    public Query<T> createQuery(Class type) {

        return getDs().createQuery(type);
    }
    public UpdateOperations<T> createUpdateOperations() {
        return this.ds.createUpdateOperations(this.entityClazz);
    }

    public WriteResult delete(T entity) {
        return this.ds.delete(entity);
    }

    public WriteResult delete(T entity, WriteConcern wc) {
        return this.ds.delete(entity, wc);
    }

    public WriteResult deleteById(K id) {
        return this.ds.delete(this.entityClazz, id);
    }

    public WriteResult deleteByQuery(Query<T> query) {
        return this.ds.delete(query);
    }

    public void ensureIndexes() {
        this.ds.ensureIndexes(this.entityClazz);
    }

    public boolean exists(String key, Object value) {
        return this.exists(this.ds.find(this.entityClazz, key, value));
    }

    public boolean exists(Query<T> query) {
        return this.ds.getCount(query) > 0L;
    }

    public QueryResults<T> find(String collection,Class type) {
        return this.createQuery(collection,type);
    }

    public QueryResults<T> find(Query<T> query) {
        return query;
    }

    public List<K> findIds() {
        return (List<K>)this.keysToIds(this.ds.find(this.entityClazz).asKeyList());
    }

    public List<K> findIds(String key, Object value) {
        return (List<K>)this.keysToIds(this.ds.find(this.entityClazz, key, value).asKeyList());
    }

    public List<K> findIds(Query<T> query) {
        return (List<K>)this.keysToIds(query.asKeyList());
    }

    public T findOne(String key, Object value) {
        return this.ds.find(this.entityClazz, key, value).get();
    }

    public T findOne(Query<T> query) {
        return query.get();
    }

    public Key<T> findOneId() {
        return this.findOneId(this.ds.find(this.entityClazz));
    }

    public Key<T> findOneId(String key, Object value) {
        return this.findOneId(this.ds.find(this.entityClazz, key, value));
    }

    public Key<T> findOneId(Query<T> query) {
        Iterator keys = query.fetchKeys().iterator();
        return keys.hasNext()?(Key)keys.next():null;
    }

    public T get(K id) {
        return this.ds.get(this.entityClazz, id);
    }

    public DBCollection getCollection(String collectionName) {
        pdCollection(collectionName);
        return ds.getDB().getCollection(collectionName);
//        return this.ds.getCollection(this.entityClazz);
    }

    public Datastore getDatastore() {
        return this.ds;
    }

    public Class<T> getEntityClass() {
        return this.entityClazz;
    }

    public Key<T> save(String collectionName,T entity) {
        return this.ds.save(collectionName,entity);

//        return this.ds.save(entity);
    }
    public Iterable<Key<T>> insert(String collectionName,Iterable<T> entitys)
    {
        return this.ds.insert(collectionName,entitys);
    }
    /**
     * 方法描述:
     * @author Jerry
     * @version V1.0
     * @date 2016/7/6 16:14
     */
    public <T> Key<T> insert(T entity)
    {
        return this.ds.insert(entity);
    }
    private void pdCollection(String name){
        if(ds.getDB().getCollection(name)==null){
            ds.getDB().createCollection(name,null);
        }
    }

    public Key<T> save(T entity, WriteConcern wc) {
        return this.ds.save(entity, wc);
    }

    public UpdateResults update(Query<T> query, UpdateOperations<T> ops) {
        return this.ds.update(query, ops);
    }
    public UpdateResults updateIfMissInsert(Query<T> query, UpdateOperations<T> ops){
        return getDs().update(query, ops,true);
    }
    public UpdateResults updateFirst(Query<T> query, UpdateOperations<T> ops) {
        return this.ds.updateFirst(query, ops);
    }
    public UpdateResults updateFirstIfMissInsert(Query<T> query, UpdateOperations<T> ops){
        return getDs().updateFirst(query, ops,true);
    }

    public DatastoreImpl getDs() {
        return this.ds;
    }

    /** @deprecated */
    public Class<T> getEntityClazz() {
        return this.entityClazz;
    }

    protected void initDS(MongoClient mongoClient, Morphia mor, String db) {
        this.ds = new DatastoreImpl(mor, mongoClient, db);
    }

    protected void initType(Class<T> type) {
        this.entityClazz = type;
        this.ds.getMapper().addMappedClass(type);
    }

    protected List<?> keysToIds(List<Key<T>> keys) {
        ArrayList ids = new ArrayList(keys.size() * 2);
        Iterator var3 = keys.iterator();

        while(var3.hasNext()) {
            Key key = (Key)var3.next();
            ids.add(key.getId());
        }

        return ids;
    }
}

