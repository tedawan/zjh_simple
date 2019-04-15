package com.addplus.server.consumer.dubbo;

import com.alibaba.dubbo.rpc.service.GenericService;

import java.util.Map;

public class DubboMap {

    private Map<String, String[]> serviceInfos;

    private Map<String, GenericService> genericServices;

    private Map<String, String> decrtipetMap;

    public Map<String, String[]> getServiceInfos() {
        return serviceInfos;
    }

    public void setServiceInfos(Map<String, String[]> serviceInfos) {
        this.serviceInfos = serviceInfos;
    }

    public Map<String, GenericService> getGenericServices() {
        return genericServices;
    }

    public void setGenericServices(Map<String, GenericService> genericServices) {
        this.genericServices = genericServices;
    }

    public Map<String, String> getDecrtipetMap() {
        return decrtipetMap;
    }

    public void setDecrtipetMap(Map<String, String> decrtipetMap) {
        this.decrtipetMap = decrtipetMap;
    }
}
