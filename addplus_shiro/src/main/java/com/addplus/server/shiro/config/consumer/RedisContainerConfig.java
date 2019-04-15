package com.addplus.server.shiro.config.consumer;

import com.addplus.server.api.constant.StringConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class RedisContainerConfig {

    private Logger logger = LoggerFactory.getLogger(RedisContainerConfig.class);


    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskScheduler threadPoolTaskExecutor() {
        logger.info("threadPoolTaskExecutor");
        ThreadPoolTaskScheduler threadPoolTaskExecutor = new ThreadPoolTaskScheduler();
        threadPoolTaskExecutor.setRemoveOnCancelPolicy(true);
        threadPoolTaskExecutor.setPoolSize(2);
        return threadPoolTaskExecutor;
    }

    /**
     * shrio消费者-用于更新菜单-角色
     */
    @Bean
    public RedieShrioConsumerService redieMessageQuenueConsumerService() {
        return new RedieShrioConsumerService();
    }

    /**
     * redis消费监听者(用于更新shiro权限资源)
     */
    @Bean(name = "redisContainer")
    public RedisMessageListenerContainer redisContainer(ThreadPoolTaskScheduler threadPoolTaskExecutor, JedisConnectionFactory getConnectionFactory) {
        logger.info("redisContainer");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setTaskExecutor(threadPoolTaskExecutor);
        container.setConnectionFactory(getConnectionFactory);
        container.addMessageListener(new MessageListenerAdapter(redieMessageQuenueConsumerService()), new ChannelTopic(StringConstant.REDIS_SHRIO_PERMISSION_TPOIC));
        return container;
    }
}
