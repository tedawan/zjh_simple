package com.addplus.server.consumer.dubbo;

import com.addplus.server.api.utils.PackageUtil;
import com.addplus.server.api.utils.security.DecriptUtil;
import com.addplus.server.consumer.utils.DubboUtil;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class DubboConfig {

    @Value("${dubbo.application.id}")
    private String applicationId;

    @Value("${dubbo.application.name}")
    private String applicationName;

    @Value("${dubbo.application.owner}")
    private String applicationOwer;

    @Value("${dubbo.registry.id}")
    private String registryId;

    @Value("${dubbo.registry.protocol}")
    private String registryProtocol;

    @Value("${dubbo.registry.timeout}")
    private Integer registryTimeout;

    @Value("${dubbo.registry.address}")
    private String registryAddress;

    @Value("${dubbo.package.name}")
    private String packageName;

    @Value("${spring.profiles.active}")
    private String active;

    @Bean
    public ApplicationConfig applicationConfig(){
        ApplicationConfig application = new ApplicationConfig();
        application.setId(applicationId);
        application.setName(applicationName);
        application.setOwner(applicationOwer);
        return application;
    }

    @Bean
    public RegistryConfig registryConfig(){
        RegistryConfig registry = new RegistryConfig();
        registry.setId(registryId);
        registry.setProtocol(registryProtocol);
        registry.setTimeout(registryTimeout);
        registry.setAddress(registryAddress);
        return registry;
    }



    @Bean
    public Set<Class<?>> getDubboPackageClass(){
        // 获取所有service
        Set<Class<?>> allClasses = new HashSet<Class<?>>();
        for (String tmp : packageName.split(","))
        {
            Set<Class<?>> classes = PackageUtil.getClasses(tmp);
            allClasses.addAll(classes);
        }
        return allClasses;
    }

    @Bean
    public DubboMap getInfos(Set<Class<?>> getDubboPackageClass,ApplicationConfig applicationConfig,RegistryConfig registryConfig){
        Map<String, String[]> serviceInfos = new HashMap<String, String[]>();
        Map<String, GenericService> genericServices = new HashMap<String, GenericService>();
        Map<String, String> decrtipetMap = new HashMap<>();
        //MonitorConfig monitorConfig = new MonitorConfig();
        //monitorConfig.setProtocol("registry");
        // 生成service所需要的容器环境
        for (Class tmp : getDubboPackageClass)
        {
            String className = tmp.getName();
            genericServices.put(className, DubboUtil.getGenericService(className, applicationConfig, registryConfig));
            Method[] methods = tmp.getMethods();
            for (Method method : methods)
            {
                Class<?>[] parameterTypes = method.getParameterTypes();
                String[] res = new String[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++)
                {
                    res[i] = parameterTypes[i].getName();
                }
                serviceInfos.put(tmp.getName() + "_" + method.getName(), res);
                decrtipetMap.put(tmp.getName() + "_" + method.getName(), DecriptUtil.SHA1(method.getName()).substring(0,16));
            }
        }
        DubboMap dubboMap = new DubboMap();
        dubboMap.setGenericServices(genericServices);
        dubboMap.setDecrtipetMap(decrtipetMap);
        dubboMap.setServiceInfos(serviceInfos);
        return dubboMap;
    }


    @Bean
    public AddplusContainer getAddplusContainer(DubboMap getInfos){
        AddplusContainer addplusContainer = AddplusContainer.newInstance(getInfos.getGenericServices(),getInfos.getServiceInfos(),getInfos.getDecrtipetMap(),packageName,active);
        return addplusContainer;
    }







}
