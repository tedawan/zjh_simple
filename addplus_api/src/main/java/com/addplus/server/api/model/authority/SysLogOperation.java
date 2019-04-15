package com.addplus.server.api.model.authority;

import com.addplus.server.api.model.base.BaseMonogoModel;
import org.mongodb.morphia.annotations.Entity;

import java.io.Serializable;

/**
 * 类名：TestShopProduct
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/4 22:28
 * @describe 类描述：日志记录操作实体类
 */
@Entity("SysLogOperation")
public class SysLogOperation extends BaseMonogoModel implements Serializable {

    private static final long serialVersionUID = -8665253035862654123L;
    /**
     * 关联用户主键Id
     */
    private String memId;

    private String method;

    private String service;

    private String param;

    private String result;

    private String module;

    private String loginType;

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getResult() {
        return result;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }


}