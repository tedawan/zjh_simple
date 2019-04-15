package com.addplus.server.api.model.authority;


import com.addplus.server.api.model.base.BaseModel;

import java.io.Serializable;

/**
 * 类名：SysMenuElement
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/10/4 22:30
 * @describe 类描述：菜单元素功能实体类
 */
public class SysMenuElement extends BaseModel implements Serializable {

    private static final long serialVersionUID = 3878163587269206957L;

    /**
     * 关联菜单主键Id
     */
    private Integer mId;

    /**
     * 当前功能描述
     */
    private String name;

    /**
     * 请求方法(url)
     */
    private String method;

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}