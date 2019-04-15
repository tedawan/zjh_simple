package com.addplus.server.shiro.config.consumer;

import com.addplus.server.shiro.config.shiro.ShiroFilerChainManager;
import com.addplus.server.shiro.service.SystemAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * 类名: RedieShrioConsumerService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/11 下午3:19
 * @description 类描述: shrio动态更新消费类
 */
public class RedieShrioConsumerService implements MessageListener
{
    private Logger logger = LoggerFactory.getLogger(SystemAdminService.class);
    @Autowired
    private ShiroFilerChainManager shiroFilerChainManager;

    @Override
    public void onMessage(Message message, byte[] bytes)
    {
        shiroFilerChainManager.initFilterChains();
        logger.info("正在更新权限");
    }
}
