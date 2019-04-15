package com.addplus.server.api.model.authority;

import com.addplus.server.api.model.base.BaseModel;

import java.io.Serializable;

/**
 * 类名：SysOrganization
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/4 22:31
 * @describe 类描述：组织结构实体类
 */
public class SysOrganization extends BaseModel implements Serializable {

    private static final long serialVersionUID = -3267069670140054593L;
    /**
     * 组织名称
     */
    private String name;

    /**
     * 0:公司 1:部门 2:小组
     */
    private Integer type;

    /**
     * 所属父级id
     */
    private Integer pid;

    /**
     * 深度，根节点为1
     */
    private Integer dept;

    /**
     * 使用逗号(,)分隔,记录到上级公司
     */
    private String path;

    /**
     * 从根节点的全部路径，使用逗号(,)分隔
     */
    private String allPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getDept() {
        return dept;
    }

    public void setDept(Integer dept) {
        this.dept = dept;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAllPath() {
        return allPath;
    }

    public void setAllPath(String allPath) {
        this.allPath = allPath;
    }
}