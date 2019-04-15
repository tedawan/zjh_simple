package com.addplus.server.provider.scheduletask;

import com.addplus.server.api.mapper.demo.SysDemoUserMapper;
import com.addplus.server.api.service.rest.demomodule.HelloService;
import com.addplus.server.connector.redis.RedisSlaveTemplateManager;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 类名: CouponOnlineStatus
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/11/8 2:57 PM
 * @description 类描述: 优惠券上架状态更改定时器
 */
@Component
public class DemoScheduleTask {

    private Logger logger = LoggerFactory.getLogger(DemoScheduleTask.class);

    @Reference(check = false)
    private HelloService helloService;

    @Autowired
    private SysDemoUserMapper demoUserMapper;

    @Autowired
    private RedisSlaveTemplateManager redisSlaveTemplateManager;

    /**
     * demo定时任务
     */
    @Scheduled(cron = "*/5 * * * * ?")
    private void setCouponNotOnline() throws Exception {
        logger.info("开始执行demo定时任务");
        logger.info(helloService.sayHello("adplus"));
        logger.info(JSON.toJSONString(demoUserMapper.getDemoUserList()));
        redisSlaveTemplateManager.getMasterTemplate().opsForValue().set("addplus", "goods");
        String conetxt = (String) redisSlaveTemplateManager.getSlaveTemlate().opsForValue().get("addplus");
        logger.info("redis获取的内容：" + conetxt);
        logger.info("demo定时任务任务结束");
    }

}
