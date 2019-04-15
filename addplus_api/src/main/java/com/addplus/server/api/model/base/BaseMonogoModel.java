package com.addplus.server.api.model.base;


import org.mongodb.morphia.annotations.Id;

import java.util.Date;

/**
 * 类名: BaseMonogoModel
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/3/5 下午2:30
 * @description 类描述: mongodb基础类
 */
public class BaseMonogoModel {

    /**
     * 主键id
     */
    @Id
    private String id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 修改人
     */
    private Integer modifyUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(Integer modifyUser) {
        this.modifyUser = modifyUser;
    }
}
