package com.addplus.server.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.addplus.server")
public class AddplusConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddplusConsumerApplication.class, args);
    }

}

