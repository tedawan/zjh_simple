package com.addplus.server.consumer.utils;


import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DubboUtil {

    private static Logger log = LoggerFactory.getLogger(DubboUtil.class);

    /**
     * 根据类名生成对应的公共DubboService
     * @param className 类名
     * @return GenericService
     */
    public static GenericService getGenericService(String className, ApplicationConfig application, RegistryConfig registry){
        try {
            ReferenceConfig<GenericService> ref = new ReferenceConfig<GenericService>();
            ref.setGeneric(true);
            ref.setRetries(0);
            ref.setTimeout(20000);
            ref.setApplication(application);
            ref.setRegistry(registry);
            ref.setCheck(false);
            ref.setInterface(className);
            //ref.setMonitor(monitorConfig);
            log.info("加载"+className);
            return ref.get();
        } catch (Exception e) {
            log.error(className+"启动失败,原因:"+e.getMessage());
        }
        return null;
    }
}
