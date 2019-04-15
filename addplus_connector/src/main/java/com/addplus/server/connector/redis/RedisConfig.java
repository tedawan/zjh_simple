package com.addplus.server.connector.redis;


import com.addplus.server.connector.system.JedisClusterFactoryCondition;
import com.addplus.server.connector.system.JedisFactoryCondition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * 类名: RedisConfig
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/1/12 下午2:04
 * @description 类描述: 用于配置redis连接类型
 */
@Configuration
@ConditionalOnProperty(name = "addplus.redis_connector",havingValue = "true")
public class RedisConfig {

    private Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.nodes}")
    private String clusterNodes;

    @Value("${spring.redis.timeout}")
    private String timeout;

    @Value("${spring.redis.max-redirects}")
    private int redirects;

    @Value("${spring.redis.maxIdle}")
    private int maxIdle;

    @Value("${spring.redis.maxTotal}")
    private int maxTotal;

    @Value("${spring.redis.maxWaitMillis}")
    private long maxWaitMillis;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.model}")
    private String model;

    /**
     * redis集群连接配置
     */
    @Bean
    public RedisClusterConfiguration getClusterConfiguration() {

        Map<String, Object> source = new HashMap<String, Object>();

        source.put("spring.redis.cluster.nodes", clusterNodes);

        source.put("spring.redis.cluster.timeout", timeout);

        source.put("spring.redis.cluster.max-redirects", redirects);

        return new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));

    }

    /**
     * redis连接池配置
     */
    @Bean
    public JedisPoolConfig getJedisPoolConfig() {
        logger.info("getJedisPoolConfig");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        return jedisPoolConfig;
    }

    /**
     * redis集群方式加载
     */
    @Bean(name = "jedisClusterConnectionFactory")
    @Conditional(JedisClusterFactoryCondition.class)
    public JedisConnectionFactory getConnectionClusterFactory(RedisClusterConfiguration getClusterConfiguration, JedisPoolConfig jedisPoolConfig) {
        logger.info("getConnectionClusterFactory");
        return new JedisConnectionFactory(getClusterConfiguration, jedisPoolConfig);
    }

    /**
     * redis主从或多节点方式加载
     */
    @Bean(value = "jedisConnectionFactory")
    @Conditional(JedisFactoryCondition.class)
    public JedisConnectionFactory getConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        logger.info("getConnectionFactory");
        String[] hosts = clusterNodes.split(",");
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        String[] hostOnly = hosts[0].split(":");
        connectionFactory.setHostName(hostOnly[0]);
        connectionFactory.setPort(Integer.valueOf(hostOnly[1]));
        if (StringUtils.isNotBlank(password)) {
            connectionFactory.setPassword(password);
        }
        return connectionFactory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate getRedisTemplate(JedisConnectionFactory getConnectionFactory) {
        logger.info("getRedisTemplate");
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(getConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }

    // 默认 key 与value 都存取string类型
    @Bean(name = "redisTemplateForPython")
    public RedisTemplate getRedisTemplateForPython(JedisConnectionFactory getConnectionFactory) {
        logger.info("getRedisTemplateForPython");
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(getConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean(name = "redisTemplateOther")
    public RedisTemplate getRedisTemplateOther(JedisConnectionFactory getConnectionFactory) {
        logger.info("getRedisTemplate");
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(getConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }


    @Bean(name = "redisSlaveTemplateManager")
    public RedisSlaveTemplateManager redisSlaveTemplateManager(RedisTemplate redisTemplate, JedisPoolConfig jedisPoolConfig) {
        logger.info("redisSlaveTemplateManager");
        Queue<RedisTemplate> slaveTemplatesQueue = null;
        if ("master".equals(model)) {
            String[] hosts = clusterNodes.split(",");
            if (hosts.length >= 2) {
                slaveTemplatesQueue = new LinkedList<>();
                JedisConnectionFactory connectionFactory = null;
                RedisTemplate redisTemplateSlave = null;
                for (int i = 1; i < hosts.length; i++) {
                    String[] hostOnly = hosts[i].split(":");
                    connectionFactory = new JedisConnectionFactory(jedisPoolConfig);

                    connectionFactory.setHostName(hostOnly[0]);
                    connectionFactory.setPort(Integer.valueOf(hostOnly[1]));
                    if (StringUtils.isNotBlank(password)) {
                        connectionFactory.setPassword(password);
                    }
                    connectionFactory.afterPropertiesSet();
                    redisTemplateSlave = new RedisTemplate();
                    redisTemplateSlave.setConnectionFactory(connectionFactory);
                    redisTemplateSlave.setKeySerializer(new StringRedisSerializer());
                    redisTemplateSlave.setDefaultSerializer(new JdkSerializationRedisSerializer());
                    redisTemplateSlave.afterPropertiesSet();
                    slaveTemplatesQueue.offer(redisTemplateSlave);
                }
            }
        }
        RedisSlaveTemplateManager redisSlaveTemplateManager = new RedisSlaveTemplateManager(redisTemplate, slaveTemplatesQueue);
        return redisSlaveTemplateManager;
    }


    @Bean
    public ListOperations getListOperations(RedisTemplate redisTemplate) {
        logger.info("getListOperations");
        return redisTemplate.opsForList();
    }

    @Bean
    public HashOperations getHashOperations(RedisTemplate redisTemplate) {
        logger.info("getHashOperations");
        return redisTemplate.opsForHash();
    }

    @Bean
    public SetOperations getSetOperations(RedisTemplate redisTemplate) {
        logger.info("getSetOperations");
        return redisTemplate.opsForSet();
    }

    @Bean
    public ValueOperations getValueOperations(RedisTemplate redisTemplate) {
        logger.info("getValueOperations");
        return redisTemplate.opsForValue();
    }

    @Bean
    public ZSetOperations getZSetOperations(RedisTemplate redisTemplate) {
        logger.info("getValueOperations");
        return redisTemplate.opsForZSet();
    }


    /**
     * redis消费订阅者线程池
     */
    @Bean(name = "otherThreadPoolTaskExecutor")
    public ThreadPoolTaskScheduler otherThreadPoolTaskExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskExecutor = new ThreadPoolTaskScheduler();
        threadPoolTaskExecutor.setRemoveOnCancelPolicy(true);
        threadPoolTaskExecutor.setPoolSize(50);
        return threadPoolTaskExecutor;
    }

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskScheduler threadPoolTaskExecutor() {
        ThreadPoolTaskScheduler threadPoolTaskExecutor = new ThreadPoolTaskScheduler();
        threadPoolTaskExecutor.setRemoveOnCancelPolicy(true);
        threadPoolTaskExecutor.setPoolSize(2);
        return threadPoolTaskExecutor;
    }

}
