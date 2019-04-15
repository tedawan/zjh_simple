package com.addplus.server.provider;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.addplus.server")
@EnableTransactionManagement
@EnableScheduling
public class AddplusProviderApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AddplusProviderApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

}

