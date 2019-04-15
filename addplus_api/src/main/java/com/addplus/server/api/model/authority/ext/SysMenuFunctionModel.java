package com.addplus.server.api.model.authority.ext;

import com.addplus.server.api.model.authority.SysMenuFunction;

import java.io.Serializable;

/**
 * Created by qiniu on 27/12/2017.
 */
public class SysMenuFunctionModel extends SysMenuFunction implements Serializable {

    /**
     * 用户是否有权限看菜单
     */
    private Boolean checked;

    /**
     * 是否半选
     */
    private Boolean halfChecked;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getHalfChecked() {
        return halfChecked;
    }

    public void setHalfChecked(Boolean halfChecked) {
        this.halfChecked = halfChecked;
    }
}
