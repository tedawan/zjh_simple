package com.addplus.server.provider.serviceimpl.web.authoritymodule;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.mapper.authority.SysRoleMapper;
import com.addplus.server.api.mapper.authority.SysRoleMenuElementMapper;
import com.addplus.server.api.mapper.authority.SysRoleMenuFunctionMapper;
import com.addplus.server.api.model.authority.SysRole;
import com.addplus.server.api.service.web.authoritymodule.RoleService;
import com.addplus.server.api.utils.DataUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuElementMapper sysRoleMenuElementMapper;

    @Autowired
    private SysRoleMenuFunctionMapper menufunctionMapper;


    @Override
    public Page<SysRole> getRoleByPage(Integer pageNo, Integer pageSize) throws Exception {
        if (pageNo == null || pageSize == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        Page<SysRole> page = new Page<SysRole>(pageNo, pageSize);
        SysRole sysRole = new SysRole();
        sysRole.setIsDeleted(0);
        page = (Page<SysRole>) sysRoleMapper.selectPage(page, new QueryWrapper<>(sysRole));
        if (page != null && page.getTotal() != 0) {
            return page;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
    }

    @Override
    public Page<SysRole> getStoreRoleByPage(Integer pageNo, Integer pageSize) throws Exception {
        if (pageNo == null || pageSize == null) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        Page<SysRole> page = new Page<SysRole>(pageNo, pageSize);
        SysRole storeSysRole = new SysRole();
        storeSysRole.setIsDeleted(0);
        storeSysRole.setType(1);
        page = (Page<SysRole>) sysRoleMapper.selectPage(page, new QueryWrapper<SysRole>(storeSysRole));
        if (page != null && page.getTotal() != 0) {
            return page;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
    }

    @Override
    public SysRole getRoleById(Long rId) throws Exception {
        boolean verify = DataUtils.EmptyOrNegativeOrZero(rId);
        if (verify) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysRole sysRole = new SysRole();
        sysRole.setId(rId);
        sysRole.setIsDeleted(0);
        SysRole sysRoleNew = sysRoleMapper.selectOne(new QueryWrapper<SysRole>(sysRole));
        if (sysRoleNew != null) {
            return sysRoleNew;
        }
        throw new ErrorException(ErrorCode.SYS_ERROR_NULLDATE);
    }

    @Override
    public Boolean addRole(SysRole sysRole) throws Exception {
        if (sysRole == null || DataUtils.EmptyOrNegative(sysRole.getType())) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        sysRole.setIsDeleted(0);
        Date date = new Date();
        sysRole.setGmtCreate(date);
        sysRole.setGmtModified(date);
        String modifyUser = RpcContext.getContext().getAttachment("id");
        sysRole.setModifyUser(modifyUser);
        int count = sysRoleMapper.insert(sysRole);
        if (count > 0) {
            return true;
        } else {
            throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL, false);
        }
    }

    @Override
    public Boolean updateRoleById(SysRole sysRole) throws Exception {
        if (sysRole == null || DataUtils.EmptyOrNegative(sysRole.getId())) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysRole sysRoleOld = sysRoleMapper.selectById(sysRole.getId());
        if (sysRoleOld != null) {
            sysRole.setGmtModified(new Date());
            String modifyUser = RpcContext.getContext().getAttachment("id");
            sysRole.setModifyUser(modifyUser);
            int count = sysRoleMapper.updateById(sysRole);
            if (count > 0) {
                return true;
            } else {
                throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL);
            }
        } else {
            throw new ErrorException(ErrorCode.SYS_ERROR_NOT_AVAILABLE);
        }
    }

    @Override
    @Transactional
    public Boolean logicallyDeleteRoleById(Long rId) throws Exception {
        boolean verify = DataUtils.EmptyOrNegativeOrZero(rId);
        if (verify) {
            throw new ErrorException(ErrorCode.SYS_ERROR_PARAM);
        }
        SysRole sysRole = new SysRole();
        sysRole.setId(rId);
        sysRole.setIsDeleted(0);
        int count = sysRoleMapper.selectCount(new QueryWrapper<SysRole>(sysRole));
        if (count > 0) {
            //逻辑删除角色表里面色内容
            String modifyUser = RpcContext.getContext().getAttachment("id");
            sysRole.setModifyUser(modifyUser);
            sysRoleMapper.updateById(sysRole);
            count = sysRoleMapper.deleteById(rId);
            if (count > 0) {
                //逻辑删除角色-菜单表内容
                menufunctionMapper.updateLogicallyDeleteByRoleId(rId);
                //逻辑删除角色-功能内容
                sysRoleMenuElementMapper.updateLogicallyDeleteByRoleId(rId);
                return true;
            } else {
                throw new ErrorException(ErrorCode.SYS_ERROR_DATABASEFAIL, false);
            }
        } else {
            throw new ErrorException(ErrorCode.SYS_ERROR_NOT_AVAILABLE, false);
        }
    }
}
