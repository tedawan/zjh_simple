package com.addplus.server.provider.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by qiniu on 15/01/2018.
 */
@Component
public class SystemBeanInitListener implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(SystemBeanInitListener.class);


    @Value("${spring.profiles.active}")
    private String actice;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //初始化
        logger.info("初始化bean后可进行操作");
    }
}
