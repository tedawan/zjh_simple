package com.addplus.server.api.model.authority;


import com.addplus.server.api.model.base.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * 类名：SysUser
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/4 22:37
 * @describe 类描述：用户实体类
 */
public class SysUser extends BaseModel implements Serializable{

    private static final long serialVersionUID = 8555973391005922017L;

    /**
     * 用户名称
     */

    private String account;

    /**
     * 存储加密后的字符
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户正式名称
     */
    private String name;

    /**
     * 0:男 1:女 2:其他
     */
    private Integer gender;

    /**
     * 出生年月日，格式“1992-10-4”
     */
    private Date birthday;

    /**
     * 多个地址使用；分隔开
     */
    private String address;

    /**
     * 所属多个组织使用,(逗号)分隔
     */
    private String oragnation;

    /**
     * 0:内部员工  1：外部员工
     */
    private Integer type;

    /**
     * 0：正®常  1：冻结
     */
    private Integer status;

    /**
     * 多个角色使用逗号（,）分隔
     */
    private String roles;

    /**
     * 用户手机号码，不能超过11位。不支持海外手机
     */
    private String phone;

    /**
     * 用户邮箱地址
     */
    private String email;

    /**
     * qq号码
     */
    private String qq;

    /**
     * 使用qq绑定的key
     */
    private String qqKey;

    /**
     * 微信号，使用微信号默认获取微信昵称
     */
    private String wechat;

    /**
     * 绑定微信的open_id
     */
    private String openId;

    /**
     * 每次登陆更新时间
     */
    private Date loginTime;

    /**
     * 登陆总次数，每登陆一次+1
     */
    private Integer loginCount;

    /**
     * 记录最后一次登陆地址
     */
    private String loginAddress;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOragnation() {
        return oragnation;
    }

    public void setOragnation(String oragnation) {
        this.oragnation = oragnation;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getQqKey() {
        return qqKey;
    }

    public void setQqKey(String qqKey) {
        this.qqKey = qqKey;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getLoginAddress() {
        return loginAddress;
    }

    public void setLoginAddress(String loginAddress) {
        this.loginAddress = loginAddress;
    }

}