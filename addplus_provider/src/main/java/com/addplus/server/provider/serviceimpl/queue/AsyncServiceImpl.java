package com.addplus.server.provider.serviceimpl.queue;


import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.service.queue.AsyncService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * 类名: AsyncService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/8/29 下午8:01
 * @description 类描述: 异步请求方法服务类
 */
@Service(interfaceClass = AsyncService.class, async = true)
public class AsyncServiceImpl implements AsyncService {

    private static Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

    private final String DELAY_HEADER = "x-delay";


    @Override
    public void sendAliYunMessage(String code, String phone) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("code", code);
       // rabbitTemplate.convertAndSend(QueueEnum.MSM_QUEUW.getExchange(), QueueEnum.MSM_QUEUW.getRoutingKey(), jsonObject);
    }

    @Override
    public void delayMessage(String conetxt) throws Exception {
        if (StringUtils.isBlank(conetxt)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        logger.info("消息发送时间：" + LocalDateTime.now());
//        rabbitTemplate.convertAndSend(QueueEnum.ORDER_QUEUE.getExchange(), QueueEnum.ORDER_QUEUE.getRoutingKey(), conetxt, message -> {
//            message.getMessageProperties().setHeader(DELAY_HEADER, 5 * 1000);
//            return message;
//        }, new CorrelationData(new ObjectId().toString()));
    }
}
