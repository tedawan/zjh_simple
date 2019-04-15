package com.addplus.server.api.model.authority.ext;

/**
 * @author fuyq
 * @date 2018/8/12
 */
public class SysRoleMenuFunctionExt {

    private Long roleId;

    private Long[] mId;


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long[] getmId() {
        return mId;
    }

    public void setmId(Long[] mId) {
        this.mId = mId;
    }
}
