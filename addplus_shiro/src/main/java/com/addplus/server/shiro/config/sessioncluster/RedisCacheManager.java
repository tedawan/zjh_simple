package com.addplus.server.shiro.config.sessioncluster;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 类名: RedisCacheManager
 *
 * @author 特大碗拉面
 * @version V1.0
 * @date 2017-10-10 16:00:15
 * @description 类描述: shrio实现cache管理
 */
public class RedisCacheManager implements CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(org.crazycake.shiro.RedisCacheManager.class);
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap();
    private RedisManager redisManager;
    private String keyPrefix = "shiro_redis_cache:";

    public RedisCacheManager() {
    }

    public String getKeyPrefix() {
        return this.keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        logger.debug("获取名称为: " + name + " 的RedisCache实例");
        Cache c = (Cache)this.caches.get(name);
        if(c == null) {
            c = new RedisCache(this.redisManager, this.keyPrefix);
            this.caches.put(name, c);
        }
        return (Cache)c;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }
}
