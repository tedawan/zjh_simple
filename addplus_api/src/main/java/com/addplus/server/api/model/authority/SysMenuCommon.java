package com.addplus.server.api.model.authority;



import com.addplus.server.api.model.base.BaseModel;

import java.io.Serializable;

/**
 * @author 
 */
public class SysMenuCommon extends BaseModel implements Serializable {

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 当前菜单描述
     */
    private String mDesc;

    /**
     * 菜单连接地址
     */
    private String url;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 模块
     */
    private String module;

    /**
     * 拦截器类型
     */
    private String filter;

    private static final long serialVersionUID = 1L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}