package com.addplus.server.connector.system;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 类名: JedisConnectionFactoryCondition
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/1/12 下午2:01
 * @description 类描述: 判断用于加载是否加载JedisConnectionFactory
 */
public class JedisFactoryCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String model =conditionContext.getEnvironment().getProperty("spring.redis.model");
        if("master".equals(model)){
            return true;
        }
        return false;
    }
}
