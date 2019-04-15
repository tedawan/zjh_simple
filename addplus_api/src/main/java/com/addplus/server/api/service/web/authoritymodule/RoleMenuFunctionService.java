package com.addplus.server.api.service.web.authoritymodule;


import com.addplus.server.api.model.authority.SysRoleMenuFunction;
import com.addplus.server.api.model.authority.ext.SysRoleMenuFunctionExt;

import java.util.List;

/**
 * 类名: RoleMenufunctionService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/11 下午1:48
 * @description 类描述: 角色-功能关联实现类
 */
public interface RoleMenuFunctionService {

    List<SysRoleMenuFunction> getRoleMenuByRoleId(Long id) throws Exception;

    List<SysRoleMenuFunction> getRoleMenuByRoleAndMenuId(Long roleId, Long menuId) throws Exception;

    Boolean addRoleMenufunction(SysRoleMenuFunction sysRoleMenufunction) throws Exception;

    Boolean logicalDeletedRoleMenufunctionById(Long id) throws Exception;

    Boolean saveRoleMenuFunction(SysRoleMenuFunctionExt sysRoleMenuFunctionExt) throws Exception;


}
