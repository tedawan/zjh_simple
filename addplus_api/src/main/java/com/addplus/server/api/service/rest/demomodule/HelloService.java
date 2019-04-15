package com.addplus.server.api.service.rest.demomodule;


import com.addplus.server.api.model.demo.SysDemoUser;

import java.util.List;

public interface HelloService {

    /**
     * 方法描述：模拟请求接口
     *
     * @param name 字符串
     * @return String
     * @throws Exception
     * @author zhangjiehang
     * @date 2018/10/10 下午2:21
     */
    String sayHello(String name);

    SysDemoUser addUser(SysDemoUser user);

    /**
      * 方法描述：模拟使用Rabbitmq发送消息，业务是发送短信
      *
      * @author zhangjiehang
      * @param code 6位验证码
      * @param phone 手机号码
      * @date 2019/1/31 11:12 AM
      * @throws Exception
      */
    String demoSendMessageByPhone(String code, String phone) throws Exception;

    /**
      * 方法描述：模拟Rabbitmy延迟队列发送
      *
      * @author zhangjiehang
      * @param context 发送文本内容
      * @date 2019/1/31 11:11 AM
      * @throws Exception
      */
    String demoDelayMessageRabbit(String context) throws Exception;

    /**
      * 方法描述：获取两个表关联一对一属性
      *
      * @author zhangjiehang
      * @date 2018/9/23 下午3:45
      * @throws Exception
      */
    SysDemoUser getDemoOneProperty(Integer demoUserId) throws Exception;

    /**
      * 方法描述：获取两个表一对多属性
      *
      * @author zhangjiehang
      * @date 2018/9/23 下午4:07
      * @throws Exception
      */
    SysDemoUser getDenoManyProperty(Integer demoUserId) throws Exception;

    /**
      * 方法描述：同时使用一对一和一对多属性
      *
      * @author zhangjiehang
      * @param
      * @return
      * @date 2018/9/23 下午4:19
      * @throws Exception
      */
    SysDemoUser getDenoOneManyProperty(Integer demoUserId) throws Exception;

    /**
      * 方法描述：获取全部demo对象，方式是读取xml文件
      *
      * @author zhangjiehang
      * @date 2019/1/31 3:17 PM
      * @throws Exception
      */
    List<SysDemoUser> getAllDemoUser() throws Exception;

}
