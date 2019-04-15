package com.addplus.server.provider.config;

import org.apache.curator.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 类名: ScheduleConfig
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/9/27 下午8:35
 * @description 类描述:多线程执行任务配置类
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("addplus-scheduled-%d").build();
        ExecutorService pool = new ScheduledThreadPoolExecutor(64,nameThreadFactory,new ThreadPoolExecutor.AbortPolicy());
        scheduledTaskRegistrar.setScheduler(pool);
    }
}
