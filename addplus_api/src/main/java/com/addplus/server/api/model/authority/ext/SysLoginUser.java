package com.addplus.server.api.model.authority.ext;


import com.addplus.server.api.model.authority.SysUser;

import java.io.Serializable;

public class SysLoginUser extends SysUser implements Serializable {

    private static final long serialVersionUID = -7137210119453236278L;

    private String verify;

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
