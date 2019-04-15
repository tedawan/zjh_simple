package com.addplus.server.api.model.base;

import java.io.Serializable;

/**
 * 类名: Token
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/6/22 上午10:43
 * @description 类描述: 接口Token实现类
 */
public class Token implements Serializable {

    private static final long serialVersionUID = 1497403970925254203L;

    /**
     * 请求密钥
     */
    private String accessKey;

    /**
     * token解密密钥
     */
    private String secretKey;

    /**
     * 用户主键信息
     */
    private String memId;


    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }
}
