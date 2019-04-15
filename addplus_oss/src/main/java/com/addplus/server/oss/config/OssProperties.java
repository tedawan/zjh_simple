package com.addplus.server.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 类描述
 *
 * @author fuyq
 * @date 2018/5/8 15:00
 */
@Component
@ConfigurationProperties(value = "ali.oss")
public class OssProperties extends OssConfig {
}
