package com.addplus.server.provider.serviceimpl.web.demomodule;


import com.addplus.server.api.service.web.authoritymodule.HelloWebService;
import com.alibaba.dubbo.config.annotation.Service;

@Service(interfaceClass = HelloWebService.class)
public class HelloWebServiceImpl implements HelloWebService {


    @Override
    public String sayHelloWeb(String name) {
        return "Hello " + name;
    }
}
