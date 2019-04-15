package com.addplus.server.consumer.dubbo;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.model.base.ReturnDataSet;
import com.addplus.server.api.utils.security.AESUtils;
import com.addplus.server.consumer.utils.SortUtil;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;


public class AddplusContainer {

    private Logger logger = LoggerFactory.getLogger(AddplusContainer.class);

    private Map<String, GenericService> getgetGenericServiceMap;

    private Map<String, String[]> getServiceInfos;

    private String packageName;

    private Map<String, String> descriptMap;

    private String active;

    private final String PROFILES_ACTIVE = "dev";

    private final String ERRORMESSAGEMODEL = "捕捉到系统异常:{0}. service: {1}, method: {2}, exception: {3}: {4}";

    private final String GENERICEXCEPTION = "com.alibaba.dubbo.rpc.service.GenericException";

    private final String ERROREXCEPTION = "com.addplus.server.api.exception.ErrorException";

    private final String RPCEXCEPTION = "com.alibaba.dubbo.rpc.RpcException";

    private final String ARGSLENGTH = "args.length != types.length";


    private static AddplusContainer addplusContainer;

    public static AddplusContainer newInstance(Map<String, GenericService> getgetGenericServiceMap, Map<String, String[]> getServiceInfos, Map<String, String> descriptMap, String packageName, String active) {
        if (addplusContainer == null) {
            addplusContainer = new AddplusContainer(getgetGenericServiceMap, getServiceInfos, descriptMap, packageName, active);
        }
        return addplusContainer;
    }


    private AddplusContainer(Map<String, GenericService> getgetGenericServiceMap, Map<String, String[]> getServiceInfos, Map<String, String> descriptMap, String packageName, String active) {
        this.getgetGenericServiceMap = getgetGenericServiceMap;
        this.getServiceInfos = getServiceInfos;
        this.descriptMap = descriptMap;
        this.packageName = packageName;
        this.active = active;
    }

    private AddplusContainer() {
    }


    public String encryptAESContent(ReturnDataSet returnDataSet, String className, String method) {
        String pName = packageName.substring(0, packageName.lastIndexOf("."));
        className = pName + "." + className;
        String invokeName = className + "_" + method;
        return AESUtils.encryptAES(JSON.toJSONString(returnDataSet), descriptMap.get(invokeName), 1);
    }


    public Map stringToMap(String className, String method, String source, Boolean encryted) {
        String pName = packageName.substring(0, packageName.lastIndexOf("."));
        className = pName + "." + className;
        String invokeName = className + "_" + method;
        if (encryted) {
            source = AESUtils.decryptAES(source, descriptMap.get(invokeName), 1);
            if (source == null) {
                return null;
            }
        }
        return JSON.parseObject(source, Map.class);
    }

    public ReturnDataSet invoke(String model, String className, String methodName, String ipAddress, Map param, Boolean httpType, String token) {
        ReturnDataSet returnDataSet = new ReturnDataSet();
        if (!httpType && null == param) {
            returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_SERVICE);
            return returnDataSet;
        }
        String genrticClass = packageName + "." + className;
        String invokeName = genrticClass + "_" + methodName;
        GenericService genericService = getgetGenericServiceMap.get(genrticClass);
        if (getServiceInfos.get(invokeName) == null) {
            // 接口不存在
            returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_PATH);
            return returnDataSet;
        }
        Map params = new HashMap(param);
        try {
            if (StringUtils.isBlank(token)) {
                token = StringConstant.NOT_TOKEN;
            }
            //使用dubbo隐式传参
            if ("rest".equals(model)) {
                Map<String, String> attachmentMap = new HashMap<String, String>();
                attachmentMap.put(StringConstant.IP_ADDRESS, ipAddress);
                attachmentMap.put(StringConstant.REQ_TOKEN_KEY, token);
                attachmentMap.put(StringConstant.MODEL, model);
                RpcContext.getContext().setAttachments(attachmentMap);
            } else {
                RpcContext.getContext().setAttachment(StringConstant.MODEL, model);
            }
            returnDataSet.setErrorCode(ErrorCode.SYS_SUCCESS);
            if (httpType) {
                returnDataSet.setDataSet(genericService.$invoke(methodName, getServiceInfos.get(invokeName), SortUtil.sortMapByKey(params)));
            } else {
                returnDataSet.setDataSet(genericService.$invoke(methodName, getServiceInfos.get(invokeName), new Object[]{params}));
            }
        } catch (Exception e) {
            if (GENERICEXCEPTION.equals(e.getClass().getName())) {
                GenericException errorException = (GenericException) e;
                if (ERROREXCEPTION.equals(errorException.getExceptionClass())) {
                    String[] errMessage = errorException.getExceptionMessage().split("_", 3);
                    if (errMessage.length > 2) {
                        returnDataSet.setDataSet(JSONObject.parse(errMessage[2]));
                    }
                    returnDataSet.setReturnCode(errMessage[0]);
                    if (PROFILES_ACTIVE.equals(active)) {
                        returnDataSet.setErrorInfo(errMessage[1]);
                    }
                } else {
                    returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_SERVICE);
                    if (PROFILES_ACTIVE.equals(active)) {
                        String errorMesage = getErrorMessage(RpcContext.getContext().getRemoteHost(), className, methodName, e.getClass().getName(), e.getMessage());
                        returnDataSet.setErrorInfo(errorMesage);
                        logger.error(errorMesage);
                    }

                }
            } else if (RPCEXCEPTION.equals(e.getClass().getName())) {
                RpcException errorException = (RpcException) e;
                if (StringUtils.isNotEmpty(errorException.getMessage())) {
                    if (errorException.getMessage().contains(ARGSLENGTH)) {
                        returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_TYPE_LENGTH);
                    } else {
                        returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_SERVICE);
                        if (PROFILES_ACTIVE.equals(active)) {
                            String errorMesage = getErrorMessage(RpcContext.getContext().getRemoteHost(), className, methodName, e.getClass().getName(), e.getMessage());
                            returnDataSet.setErrorInfo(errorMesage);
                            logger.error(errorMesage);
                        }

                    }
                }

            } else {
                e.printStackTrace();
                returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_SERVICE);
                if (PROFILES_ACTIVE.equals(active)) {
                    String errorMesage = getErrorMessage(RpcContext.getContext().getRemoteHost(), className, methodName, e.getClass().getName(), e.getMessage());
                    returnDataSet.setErrorInfo(errorMesage);
                    logger.error(errorMesage);
                }
            }
        }
        return returnDataSet;
    }

    private String getErrorMessage(String... args) {
        String error = MessageFormat.format(ERRORMESSAGEMODEL, args);
        return error;
    }


}
