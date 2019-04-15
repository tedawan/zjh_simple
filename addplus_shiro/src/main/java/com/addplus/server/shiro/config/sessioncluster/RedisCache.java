package com.addplus.server.shiro.config.sessioncluster;

import com.addplus.server.api.model.base.BaseModel;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.crazycake.shiro.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 类名: RedisCache
 *
 * @author 特大碗拉面
 * @version V1.0
 * @date 2017-10-10 15:59:43
 * @description 类描述: shrio自定义缓存必须实现Cache<K,V>
 */
public class RedisCache<K,V> implements Cache<K,V> {

    private Logger logger;
    private RedisManager cache;
    private String keyPrefix;

    public String getKeyPrefix() {
        return this.keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public RedisCache(RedisManager cache) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.keyPrefix = "shiro_redis_session:";
        if(cache == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        } else {
            this.cache = cache;
        }
    }

    public RedisCache(RedisManager cache, String prefix) {
        this(cache);
        this.keyPrefix = prefix;
    }

    private String getStringKey(K key){
        if(key instanceof String){
            return this.keyPrefix + key;
        }else{
            if("org.apache.shiro.subject.SimplePrincipalCollection".equals(key.getClass().getName())){
                SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) key;
                BaseModel user = (BaseModel) principalCollection.getPrimaryPrincipal();
                return this.keyPrefix + user.getId();
            }
            return JSON.toJSONString(SerializeUtils.serialize(key));
        }
    }
    @Override
    public V get(K key) throws CacheException {
        this.logger.debug("根据key从Redis中获取对象 key [" + key + "]");

        try {
            if(key == null) {
                return null;
            } else {
                Object value = this.cache.get(this.getStringKey(key));
                if(value==null){
                    return null;
                }
                return (V) value;
            }
        } catch (Throwable var4) {
            throw new CacheException(var4);
        }
    }
    @Override
    public V put(K key, V value) throws CacheException {
        this.logger.debug("根据key从存储 key [" + key + "]");

        try {
            this.cache.set(this.getStringKey(key),value);
            return value;
        } catch (Throwable var4) {
            throw new CacheException(var4);
        }
    }
    @Override
    public V remove(K key) throws CacheException {
        this.logger.debug("从redis中删除 key [" + key + "]");

        try {
            V previous = this.get(key);
            this.cache.del(this.getStringKey(key));
            return previous;
        } catch (Throwable var3) {
            throw new CacheException(var3);
        }
    }
    @Deprecated
    @Override
    public void clear() throws CacheException {
        this.logger.debug("从redis中删除所有元素");
        this.cache.del("shiro_redis_session:*");
    }

    @Deprecated
    @Override
    public int size() {
        try {
            Set keys = this.cache.keys(this.keyPrefix + "*");
            return keys.size();
        } catch (Throwable var2) {
            throw new CacheException(var2);
        }
    }
    @Override
    public Set<K> keys() {
        try {
            Set keys = this.cache.keys(this.keyPrefix + "*");
            if(CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            } else {
                Set<K> newKeys = new HashSet();
                Iterator iterator = keys.iterator();

                while(iterator.hasNext()) {
                    byte[] key = (byte[])iterator.next();
                    newKeys.add((K)SerializeUtils.deserialize(key));
                }

                return newKeys;
            }
        } catch (Throwable var5) {
            throw new CacheException(var5);
        }
    }
    @Override
    public Collection<V> values() {
        try {
            Set<byte[]> keys = this.cache.keys(this.keyPrefix + "*");
            if(!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList(keys.size());
                Iterator iterator = keys.iterator();

                while(iterator.hasNext()) {
                    byte[] key = (byte[])iterator.next();
                    V value = this.get((K)SerializeUtils.deserialize(key));
                    if(value != null) {
                        values.add(value);
                    }
                }

                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable var6) {
            throw new CacheException(var6);
        }
    }
}
