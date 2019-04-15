package com.addplus.server.api.model.base;

import com.addplus.server.api.constant.ErrorCode;

import java.io.Serializable;

/**
 * @author limo
 * @version V1.0
 * @description APP接口统一返回类型
 * @date 2016年5月11日
 */
public class ReturnDataSet implements Serializable {
    private static final long serialVersionUID = 6171110719104783248L;

    private Object dataSet = "";

    private String errorInfo = "";

    private String returnCode = "";

    public ReturnDataSet() {
    }

    public ReturnDataSet(ErrorCode errorCode, Object dataSet) {
        this.returnCode = errorCode.getCode();
        this.errorInfo = errorCode.getError();
        this.dataSet = dataSet;
    }

    public void setErrorCode(ErrorCode errorCode) {
        setErrorInfo(errorCode.getError());
        setReturnCode(errorCode.getCode());
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Object getDataSet() {
        return dataSet;
    }

    public void setDataSet(Object dataSet) {
        this.dataSet = dataSet;
    }


}
