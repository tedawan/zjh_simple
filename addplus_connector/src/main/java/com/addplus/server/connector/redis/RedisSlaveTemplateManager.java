package com.addplus.server.connector.redis;


import org.springframework.data.redis.core.RedisTemplate;

import java.util.Queue;

public class RedisSlaveTemplateManager {

    /**
     * 最开始初始化的redistemlate
     */
    private RedisTemplate redisTemplate;

    /**
     * 从节点redistemlate
     */
    private Queue<RedisTemplate> slaveTemplatesQueue;

    private RedisSlaveTemplateManager() {
    }

    public RedisSlaveTemplateManager(RedisTemplate redisTemplate, Queue<RedisTemplate> slaveTemplatesQueue) {
        this.redisTemplate = redisTemplate;
        this.slaveTemplatesQueue = slaveTemplatesQueue;
    }

    /**
     * 方法描述：获取从节点的redisTemplate
     *
     * @param
     * @return
     * @throws Exception
     * @author zhangjiehang
     * @date 2018/1/12 下午12:08
     */
    public RedisTemplate getSlaveTemlate() {
        if (slaveTemplatesQueue != null && !slaveTemplatesQueue.isEmpty()) {
            if (slaveTemplatesQueue.size() == 1) {
                return slaveTemplatesQueue.peek();
            }
            RedisTemplate redisTemplate = slaveTemplatesQueue.poll();
            if (redisTemplate == null) {
                return this.redisTemplate;
            } else {
                slaveTemplatesQueue.offer(redisTemplate);
            }
            return redisTemplate;
        } else {
            //当启动集群的时候，或者以后修改成单节点时候，以免已有代码报错，都使用主节点
            return redisTemplate;
        }
    }


    /**
      * 方法描述：获取主节点master
      *
      * @author zhangjiehang
      * @param
      * @return
      * @date 2018/1/12 下午2:35
      * @throws Exception
      */
    public RedisTemplate getMasterTemplate(){
        return redisTemplate;
    }

}
