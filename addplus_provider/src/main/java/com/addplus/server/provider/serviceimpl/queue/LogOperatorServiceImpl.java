package com.addplus.server.provider.serviceimpl.queue;

import com.addplus.server.api.model.authority.SysLogOperation;
import com.addplus.server.api.mongodao.SysLogOperationDao;
import com.addplus.server.api.service.queue.LogOperatorService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Service(interfaceClass = LogOperatorService.class, async = true)
public class LogOperatorServiceImpl implements LogOperatorService {

    @Autowired
    private SysLogOperationDao logOperationDao;

    @Override
    public void logRecordOperator(JSONObject map) {

        SysLogOperation logOperation = new SysLogOperation();
        Object paramObj = map.get("param");
        if (paramObj != null) {
            Object[] param = (Object[]) paramObj;
            Object paramNameObj = map.get("paramName");
            if (paramNameObj != null) {
                String[] paramName = (String[]) paramNameObj;
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < param.length; i++) {
                    jsonObject.put(paramName[i], param[i]);
                }
                if (jsonObject != null && !jsonObject.isEmpty()) {
                    logOperation.setParam(JSON.toJSONString(jsonObject));
                }
            }
        }
        logOperation.setMethod(map.get("method").toString());
        logOperation.setService(map.get("service").toString());
        Object memId = map.get("memId");
        if (memId != null) {
            logOperation.setMemId(memId.toString());
        }
        Object loginType = map.get("loginType");
        if (loginType != null) {
            logOperation.setLoginType(loginType.toString());
        }
        Object resultObj = map.get("result");
        if (resultObj != null) {
            logOperation.setResult(JSON.toJSONString(resultObj));
        }
        logOperation.setGmtCreate(new Date());
        logOperation.setModule(map.get("module").toString());
        logOperationDao.insert(logOperation);

    }
}
