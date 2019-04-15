package com.addplus.server.shiro.config.system;

import com.addplus.server.shiro.config.shiro.ShiroFilerChainManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by qiniu on 15/01/2018.
 */
@Component
public class SystemBeanInitListener implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(SystemBeanInitListener.class);

    @Autowired
    private ShiroFilerChainManager shiroFilerChainManager;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //初始化shiro拦截链表
        shiroFilerChainManager.initFilterChains();
        logger.info("shiroFilerChainManager init!");
    }
}
