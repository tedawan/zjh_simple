package com.addplus.server.api.service.queue;


/**
 * 类名: AsyncService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/8/29 下午8:01
 * @description 类描述: 异步请求方法服务类
 */
public interface AsyncService {

    /**
     * 方法描述：发送对应验证码信息，注意对应Redis存储位置不一致
     *
     * @param code  验证码
     * @param phone 手机号码
     * @throws Exception
     * @author zhangjiehang
     * @date 2018/9/20 下午4:36
     */
    void sendAliYunMessage(String code, String phone) throws Exception;

    /**
      * 方法描述：队列延迟消息发送
      *
      * @author zhangjiehang
      * @param conetxt 发送内容
      * @return
      * @date 2019/1/31 2:32 PM
      * @throws Exception
      */
    void delayMessage(String conetxt) throws Exception;


}
