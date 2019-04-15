package com.addplus.server.api.service.web.authoritymodule;

import com.addplus.server.api.model.authority.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface UserService {

    SysUser selectByUsername(String account);

    Boolean updateLoginUser(SysUser sysUser) throws Exception;

    Boolean addUser(SysUser sysUser) throws Exception;

    Boolean deleteUserById(Long id) throws Exception;

    SysUser selectUserById(Long id) throws Exception;

    SysUser modifyUserGetInfoById(Long id) throws Exception;

    String encryptString(SysUser sysUser) throws Exception;

    Boolean updateUser(SysUser sysUser) throws Exception;

    Page<SysUser> getAllUsers(Integer pageNo, Integer pageSize) throws Exception;

    SysUser getByUser() throws Exception;

    Integer getUserNameCount(String account) throws Exception;

}
