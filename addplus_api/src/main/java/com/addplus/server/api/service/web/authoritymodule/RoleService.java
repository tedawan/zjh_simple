package com.addplus.server.api.service.web.authoritymodule;


import com.addplus.server.api.model.authority.SysRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface RoleService{


    Page<SysRole> getRoleByPage(Integer pageNo, Integer pageSize) throws Exception;

    Page<SysRole> getStoreRoleByPage(Integer pageNo, Integer pageSize) throws Exception;

    SysRole getRoleById(Long rId) throws Exception;

    Boolean addRole(SysRole sysRole) throws Exception;

    Boolean updateRoleById(SysRole sysRole) throws Exception;

    Boolean logicallyDeleteRoleById(Long rId) throws Exception;
}
