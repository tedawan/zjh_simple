package com.addplus.server.consumer.config.aspect;

import com.alibaba.dubbo.rpc.RpcContext;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 类名: WebLogAspect
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/12 下午3:27
 * @description 类描述: 记录请求方法以及运行时间
 */
@Aspect
@Component
public class UserAspect {
    private Logger logger = LoggerFactory.getLogger(UserAspect.class);

    /**
     * 定义一个切入点.
     * 解释下：
     *
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 任意包名
     * ~ 第三个 * 代表任意方法.
     * ~ 第四个 * 定义在web包或者子包
     * ~ 第五个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */
    //两个..代表所有子目录，最后括号里的两个..代表所有参数
    @Pointcut("execution(public * com.addplus.server.consumer.action..*.*(..))")
    public void UserPointCut() {
    }

    @Before("UserPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // aop切面传递用户主键id到服务提供者
        Object idOb= SecurityUtils.getSubject().getSession().getAttribute("id");
        Object loginType = SecurityUtils.getSubject().getSession().getAttribute("loginType");
        if (idOb!=null){
            RpcContext.getContext().setAttachment("id", idOb.toString());
        }
        if (loginType != null) {
            RpcContext.getContext().setAttachment("loginType", loginType.toString());
        }
    }

}
