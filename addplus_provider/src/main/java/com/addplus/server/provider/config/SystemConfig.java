package com.addplus.server.provider.config;

import org.apache.shiro.crypto.hash.DefaultHashService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SystemConfig {


    @Bean
    public DefaultHashService defaultHashService(){
        DefaultHashService hashService = new DefaultHashService(); //默认算法SHA-512
        hashService.setHashAlgorithmName("md5");
        hashService.setHashIterations(2); //生成Hash值的迭代次数
        return hashService;
    }
}
