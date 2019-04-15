package com.addplus.server.provider.serviceimpl.web.authoritymodule;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysMenuElementMapper;
import com.addplus.server.api.mapper.authority.SysMenuFunctionMapper;
import com.addplus.server.api.mapper.authority.SysUserMapper;
import com.addplus.server.api.model.authority.SysMenuElement;
import com.addplus.server.api.model.authority.SysMenuFunction;
import com.addplus.server.api.model.authority.SysUser;
import com.addplus.server.api.model.authority.ext.SysMenuFunctionUser;
import com.addplus.server.api.service.web.authoritymodule.UserAuthorityService;
import com.addplus.server.api.utils.MenuFunctionTreeUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名: UserAuthorityServiceImpl
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/4 下午4:40
 * @description 类描述: 用户权限实现类
 */
@Service(interfaceClass = UserAuthorityService.class)
public class UserAuthorityServiceImpl implements UserAuthorityService {

    @Autowired
    private SysMenuFunctionMapper sysMenuFunctionMapper;

    @Autowired
    private SysMenuElementMapper sysMenuElementMapper;

    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public List<SysMenuFunctionUser> getUserMenuFunctionByUserId() throws Exception {
        //把菜单信息存入用户的session
        String id = RpcContext.getContext().getAttachment("id");
        if (StringUtils.isBlank(id)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }
        String roleAll[] = null;
        roleAll = ((SysUser) getByUserBySingle()).getRoles().split(",");
        if (roleAll == null || roleAll.length == 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        } else {
            List<Integer> roles = new ArrayList<>();
            for (String r : roleAll) {
                roles.add(Integer.valueOf(r));
            }
            List<SysMenuFunction> sysMenuFunctionList = sysMenuFunctionMapper.getMenuFunctionUserAll(roles);
            if (sysMenuFunctionList == null || sysMenuFunctionList.isEmpty()) {
                throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
            }
            List<SysMenuFunctionUser> menuTree = MenuFunctionTreeUtils.getMenuFunctionTree(sysMenuFunctionList);
            if (menuTree == null || menuTree.isEmpty()) {
                throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
            }
            return menuTree;
        }
    }

    @Override
    public List<SysMenuElement> getUserMenuElemnetList(Integer mId) throws Exception {
        //获取用户的信息
        String id = RpcContext.getContext().getAttachment("id");
        if (StringUtils.isBlank(id)) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }
        String role = role = ((SysUser) getByUserBySingle()).getRoles();
        ;

        String[] roleAll = role.split(",");
        if (roleAll.length == 0) {
            throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
        }
        List<Integer> roles = new ArrayList<>();
        for (String r : roleAll) {
            roles.add(Integer.valueOf(r));
        }
        List<SysMenuElement> elementList = sysMenuElementMapper.getUserMenuElementList(roles, mId);
        if (elementList != null && !elementList.isEmpty()) {
            return elementList;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
    }

    @Override
    public JSONObject getByUser() throws Exception {
        Object o = RpcContext.getContext().getAttachment("id");
        Integer memId = Integer.valueOf(RpcContext.getContext().getAttachment("id"));
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", memId);
        queryWrapper.eq("is_deleted", 0);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSON(sysUserMapper.selectOne(queryWrapper)).toString());
        return jsonObject;
    }

    private Object getByUserBySingle() throws Exception {
        Integer memId = Integer.valueOf(RpcContext.getContext().getAttachment("id"));
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", memId);
        queryWrapper.eq("is_deleted", 0);
        return sysUserMapper.selectOne(queryWrapper);
    }


}
