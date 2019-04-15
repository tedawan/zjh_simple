package com.addplus.server.shiro.config.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 类名: CustomizedToken
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/10/27 4:37 PM
 * @description 类描述: 自定义UsernamePasswordToken
 */
public class CustomizedToken extends UsernamePasswordToken {

    private static final long serialVersionUID = -6501085735527859716L;

    //登录类型，判断是普通用户登录，教师登录还是管理员登录
    private LoginType loginType;

    public CustomizedToken(final String username, final String password, LoginType loginType) {
        super(username, password);
        this.loginType = loginType;
    }

    public LoginType getLoginType() {
        return loginType;
    }

}
