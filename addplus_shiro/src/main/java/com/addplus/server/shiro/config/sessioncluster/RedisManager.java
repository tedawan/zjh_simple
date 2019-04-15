package com.addplus.server.shiro.config.sessioncluster;

import com.addplus.server.connector.redis.RedisSlaveTemplateManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 类名: RedisManager
 *
 * @author 特大碗拉面
 * @version V1.0
 * @date 2017-10-10 15:58:53 15:58
 * @description 类描述: Redis的连接操作类
 */
public class RedisManager{

    private RedisTemplate redisTemplate;

    private ValueOperations operations;

    private RedisSlaveTemplateManager slaveTemplateManager;

    private ValueOperations operationsSlave;

    private int expire = 0;

    public RedisManager(RedisTemplate redisTemplate, RedisSlaveTemplateManager slaveTemplateManager) {
        this.redisTemplate = redisTemplate;
        this.operations = redisTemplate.opsForValue();
        this.slaveTemplateManager = slaveTemplateManager;
        this.operationsSlave = slaveTemplateManager.getSlaveTemlate().opsForValue();
    }

    public Object get(String key) {
        return operationsSlave.get(key);
    }

    public Object set(String key, Object value) {
        if (this.getExpire() != 0) {
            operations.set(key, value,this.expire,TimeUnit.SECONDS);
        }else{
            operations.set(key, value);
        }

        return value;
    }

    public Object set(String key, Object value, int expire) {
        if (expire != 0) {
            operations.set(key, value,expire,TimeUnit.SECONDS);
        }else{
            operations.set(key, value);
        }

        return value;
    }

    public void del(String key) {
            redisTemplate.delete(key);
    }

    public Set keys(String pattern) {
        Set keys = slaveTemplateManager.getSlaveTemlate().keys(pattern);
        return keys;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
