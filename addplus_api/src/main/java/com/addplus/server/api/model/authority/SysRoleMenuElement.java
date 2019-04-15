package com.addplus.server.api.model.authority;


import com.addplus.server.api.model.base.BaseModel;

import java.io.Serializable;

/**
 * 类名：RoleButton
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/4 22:34
 * @describe 类描述：角色与功能元素关联实体类
 */
public class SysRoleMenuElement extends BaseModel implements Serializable {

    private static final long serialVersionUID = -5953024476244653914L;
    /**
     * 关联功能元素主键Id
     */
    private Integer eId;

    /**
     * 关联角色id
     */
    private Integer rId;

    private Integer isDelete;

    public Integer geteId() {
        return eId;
    }

    public void seteId(Integer eId) {
        this.eId = eId;
    }

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}