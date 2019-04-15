package com.addplus.server.api.service.web.authoritymodule;


import com.addplus.server.api.model.authority.SysMenuFunction;
import com.addplus.server.api.model.authority.ext.SysMenuFunctionUser;
import com.addplus.server.api.model.authority.ext.SysRoleMenuFunctionUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 类名: MenuService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/4 上午11:35
 * @description 类描述: 菜单的增删改查
 */
public interface MenuFunctionService {

    SysMenuFunction getMenuFunction(Long id) throws Exception;

    Page<SysMenuFunctionUser> getMenuFunctionByPage(Integer pageNo, Integer pageSize) throws Exception;

    Page<SysMenuFunctionUser> getMenuPageByType(Integer pageNo, Integer pageSize, Integer type) throws Exception;

    Page<SysRoleMenuFunctionUser> getMenuFunctionByPageWithRole(Integer pageNo, Integer pageSize, Long roleId) throws Exception;

    Boolean updateMenuFunctionById(SysMenuFunction sysMenuFunction) throws Exception;

    Boolean deleteMenuFunctionById(Integer id) throws Exception;

    SysMenuFunction addMenuFunction(SysMenuFunction sysMenuFunction) throws Exception;

    Page<SysMenuFunction> searchMenuFunctionByName(String name, Integer pageNo, Integer pagesize, String pid) throws Exception;

    Page<SysMenuFunction> getMenuFunctionListByPage(Integer pageNo, Integer pageSize) throws Exception;

    Integer refreshMenu();

    Boolean refreshAuthority() throws Exception;
}
