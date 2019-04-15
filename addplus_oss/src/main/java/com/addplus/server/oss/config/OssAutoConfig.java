package com.addplus.server.oss.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author fuyq
 * @date 2018/8/30
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfig {

    @Autowired
    private OssProperties ossProperties;

    @Bean
    @Primary
    OssConfig ossConfig() {
        return ossProperties;
    }
}
