package com.addplus.server.api.model.authority;

import com.addplus.server.api.model.base.BaseModel;

import java.io.Serializable;

/**
 * 类名：RoleMenufunction
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/4 22:35
 * @describe 类描述：角色菜单功能关联实体类
 */
public class SysRoleMenuFunction extends BaseModel implements Serializable {

    private static final long serialVersionUID = -2170632392591067624L;
    /**
     * 关联角色主键Id
     */
    private Long rId;

    /**
     * 关联菜单主键id
     */
    private Long mId;

    public Long getrId() {
        return rId;
    }

    public void setrId(Long rId) {
        this.rId = rId;
    }

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }
}