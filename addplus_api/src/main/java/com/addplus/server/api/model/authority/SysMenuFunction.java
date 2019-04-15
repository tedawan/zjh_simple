package com.addplus.server.api.model.authority;


import com.addplus.server.api.model.base.BaseModel;

import java.io.Serializable;

/**
 * 类名：MemuFunction
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/4 22:29
 * @describe 类描述：菜单实体类
 */
public class SysMenuFunction extends BaseModel implements Serializable {

    private static final long serialVersionUID = 544826725032268032L;
    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 当前菜单描述
     */
    private String mDesc;

    /**
     * 菜单深度，一级菜单为1
     */
    private Integer dept;

    /**
     * 菜单连接地址
     */
    private String url;

    /**
     * 所属上级菜单（-1代表是根节点）
     */
    private Long pid;

    /**
     * 类型(0:内部 1:外部 )
     */
    private Integer type;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 当前菜单路径，使用逗号(,)分隔
     */
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public Integer getDept() {
        return dept;
    }

    public void setDept(Integer dept) {
        this.dept = dept;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}