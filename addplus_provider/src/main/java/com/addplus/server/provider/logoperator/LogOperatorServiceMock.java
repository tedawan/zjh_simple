package com.addplus.server.provider.logoperator;
import com.addplus.server.api.service.queue.LogOperatorService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogOperatorServiceMock implements LogOperatorService {

    private static Logger logger = LoggerFactory.getLogger(LogOperatorServiceMock.class);


    @Override
    public void logRecordOperator(JSONObject map) {
        logger.info(JSON.toJSONString(map));
    }

}
