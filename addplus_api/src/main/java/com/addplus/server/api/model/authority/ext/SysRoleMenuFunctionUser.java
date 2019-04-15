package com.addplus.server.api.model.authority.ext;


import java.util.List;

/**
 * Created by qiniu on 27/12/2017.
 */
public class SysRoleMenuFunctionUser extends SysMenuFunctionModel {
    private List<SysRoleMenuFunctionUser> children;

    public List<SysRoleMenuFunctionUser> getChildren() {
        return children;
    }

    public void setChildren(List<SysRoleMenuFunctionUser> children) {
        this.children = children;
    }
}
