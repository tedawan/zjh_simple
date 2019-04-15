package com.addplus.server.provider.config;

import com.addplus.server.api.utils.PackageUtil;
import com.addplus.server.provider.annotation.NotToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class TokenAnnotationConfig {

    @Value("${annotation.package.name}")
    private String packageName;


    @Bean(name = "tokenAnnontationMap")
    public Map<String,Boolean> getTokenAnnontation() {
        Map<String, Boolean> map = new HashMap<>();
        // 获取所有service
        Set<Class<?>> allClasses = new HashSet<Class<?>>();
        for (String tmp : packageName.split(",")) {
            Set<Class<?>> classes = PackageUtil.getClasses(tmp);
            allClasses.addAll(classes);
        }
        for (Class tmp : allClasses) {
            Method[] methods = tmp.getMethods();
            for (Method method : methods) {
                Annotation[] annotations = method.getAnnotations();
                if (annotations.length>0){
                    if (annotations[0].annotationType().equals(NotToken.class)){
                        System.out.println();
                        map.put(tmp.getInterfaces()[0].getName()+ "_" + method.getName(), true);
                    }
                }
            }
        }
        return map;
    }


}
