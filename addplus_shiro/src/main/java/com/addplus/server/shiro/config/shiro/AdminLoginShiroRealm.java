package com.addplus.server.shiro.config.shiro;

import com.addplus.server.api.mapper.authority.SysMenuElementMapper;
import com.addplus.server.api.mapper.authority.SysUserMapper;
import com.addplus.server.api.model.authority.SysUser;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类名: AdminLoginShiroRealm
 *
 * @author 特大碗拉面
 * @version V1.0
 * @date 2017-10-17 17:00:15
 * @description 类描述: 登录基本鉴权器
 */
public class AdminLoginShiroRealm extends AuthorizingRealm {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysMenuElementMapper sysMenuElementMapper;

    @Value("${spring.redis.expire}")
    private int expire;

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 查询用户角色，拥有什么权限，放到SimpleAuthorizationInfo里面quide 、
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Object user = SecurityUtils.getSubject().getPrincipal();
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSON(user).toString());
        if (jsonObject != null && !jsonObject.isEmpty()) {
            String role = jsonObject.getString("roles");
            if (StringUtils.isNotBlank(role)) {
                String[] roles = role.split(",");
                List<String> list = Arrays.asList(roles);
                if (!list.isEmpty()) {
                    info.addRoles(list);
                }
            }
        }
        return info;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // 获取用户的输入的账号.
        String account = (String) authenticationToken.getPrincipal();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",account);
        queryWrapper.eq("is_deleted",0);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        if (sysUser == null) {
            throw new UnknownAccountException();
        }
        if (1 == sysUser.getStatus()) {
            // 帐号锁定
            throw new LockedAccountException();
        }
        Session session = SecurityUtils.getSubject().getSession();
        session.setTimeout(expire * 1000L);
        session.setAttribute("id", sysUser.getId());
        session.setAttribute("loginType", LoginType.ADMIN.getType());
        String rolesString = sysUser.getRoles();
        if (StringUtils.isNotBlank(rolesString)) {
            List<String> roles = Arrays.asList(rolesString.split(","));
            if (!roles.isEmpty()) {
                List<Integer> list = roles.stream().filter(o -> StringUtils.isNotBlank(o)).map(o -> Integer.parseInt(o)).collect(Collectors.toList());
                session.setAttribute("roles", list);
            }
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(), ByteSource.Util.bytes(account), getName());
        return authenticationInfo;
    }
}
