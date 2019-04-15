package com.addplus.server.provider.logoperator;

import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.service.queue.LogOperatorService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 类名: LogRecord
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/6/23 下午2:58
 * @description 类描述: 记录访问日志aop切面操作
 */
@Aspect
@Component
@ConditionalOnProperty(name = "log.record.operator",havingValue = "true")
public class LogRecord {

    @Reference(async = true,mock="com.addplus.server.provider.logoperator.LogOperatorServiceMock")
    private LogOperatorService logOperatorService;

    @Pointcut("execution(public * com.addplus.server.provider.serviceimpl..*.*(..))")
    public void LogPointCut() {
    }

    @AfterReturning(returning = "ret", pointcut = "LogPointCut()")// returning的值和doAfterReturning的参数名一致
    public void doAfterReturning(JoinPoint joinPoint,Object ret) throws Throwable {
        if (logOperatorService==null){
            return;
        }
        // 接收到请求，记录请求内容
        JSONObject map = new JSONObject();
        //获取参数名称
        String[] paramNames=((CodeSignature) joinPoint.getSignature()).getParameterNames();
        if (paramNames!=null&&paramNames.length>0){
            map.put("paramName",paramNames);
        }
        Object[] args = joinPoint.getArgs();
        if (args!=null&&args.length>0){
            map.put("param",args);
        }
        map.put("service",joinPoint.getSignature().getDeclaringTypeName());
        map.put("method",joinPoint.getSignature().getName());
        String memId = RpcContext.getContext().getAttachment("memId");
        if (StringUtils.isNotBlank(memId)){
            memId = RpcContext.getContext().getAttachment("id");
            if (StringUtils.isNotBlank(memId)){
                map.put("memId",memId);
            }
        }
        String loginType = RpcContext.getContext().getAttachment("loginType");
        if (StringUtils.isNotBlank(memId)) {
            map.put("loginType", loginType);
        }
        if (ret!=null){
            map.put("result",ret);
        }
        String model = RpcContext.getContext().getAttachment(StringConstant.MODEL);
        map.put("module",model);
        logOperatorService.logRecordOperator(map);

    }

}
