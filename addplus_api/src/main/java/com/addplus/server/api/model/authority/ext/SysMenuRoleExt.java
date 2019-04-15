package com.addplus.server.api.model.authority.ext;

public class SysMenuRoleExt {

    /**
     * 菜单角色id
     */
    private Integer mId;

    /**
     * 关联url
     */
    private String url;

    /**
     * 角色主键id
     */
    private String rId;

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }
}
