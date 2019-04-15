package com.addplus.server.api.model.authority;


import com.addplus.server.api.model.base.BaseModel;

import java.io.Serializable;

/**
 * 类名：SysRole
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/4 22:33
 * @describe 类描述：角色实体类
 */
public class SysRole extends BaseModel implements Serializable {

    private static final long serialVersionUID = -4644106803129090749L;
    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述内容
     */
    private String rDesc;

    /**
     * 0:内部 1:外部
     */
    private Integer type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getrDesc() {
        return rDesc;
    }

    public void setrDesc(String rDesc) {
        this.rDesc = rDesc;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}