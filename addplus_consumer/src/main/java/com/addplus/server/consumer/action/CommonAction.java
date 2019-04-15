package com.addplus.server.consumer.action;

import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.model.base.ReturnDataSet;
import com.addplus.server.consumer.dubbo.AddplusContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
public class CommonAction {

    @Autowired
    private AddplusContainer addplusContainer;

    @Value("${param.encryted}")
    private Boolean encryted;


    @GetMapping(value = "/get/{model}/{module}/{service}/{method}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object get(@PathVariable String model, @PathVariable String module, @PathVariable String service, @PathVariable String method, @RequestParam Map map, @RequestHeader Map<String, String> headerMap) {
        ReturnDataSet returnDataSet = addplusContainer.invoke(model, model + "." + module + "." + service, method, this.getIpAddress(headerMap), map, true, headerMap.getOrDefault(StringConstant.TOKEN, null));
        if (encryted) {
            return addplusContainer.encryptAESContent(returnDataSet, model + "." + module + "." + service, method);
        }
        return returnDataSet;
    }

    @PostMapping(value = "/post/{model}/{module}/{service}/{method}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object post(@PathVariable String model, @PathVariable String module, @PathVariable String service, @PathVariable String method, @RequestBody String contentStr, @RequestHeader Map<String, String> headerMap) {
        Map param = addplusContainer.stringToMap(model + "." + module + "." + service, method, contentStr, encryted);
        ReturnDataSet returnDataSet = addplusContainer.invoke(model, model + "." + module + "." + service, method, getIpAddress(headerMap), param, false, headerMap.getOrDefault(StringConstant.TOKEN, null));
        if (encryted) {
            return addplusContainer.encryptAESContent(returnDataSet, model + "." + module + "." + service, method);
        }
        return returnDataSet;
    }


    /**
     * 方法描述：获取nginx配置的ip地址
     *
     * @param
     * @return
     * @throws Exception
     * @author zhangjiehang
     * @date 2019/2/13 11:43 AM
     */
    private String getIpAddress(Map<String, String> headerMap) {
        String ip = headerMap.get("x-real-ip");//先从nginx自定义配置获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headerMap.get("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headerMap.get("Host");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headerMap.get("host");
        }
        return ip;
    }

}
