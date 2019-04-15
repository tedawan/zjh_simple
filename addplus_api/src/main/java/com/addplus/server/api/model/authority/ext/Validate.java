package com.addplus.server.api.model.authority.ext;

import java.io.Serializable;

public class Validate implements Serializable {

    private static final long serialVersionUID = -2007502108369872835L;

    private String base64;

    private String code;

    private String vToken;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getvToken() {
        return vToken;
    }

    public void setvToken(String vToken) {
        this.vToken = vToken;
    }
}
