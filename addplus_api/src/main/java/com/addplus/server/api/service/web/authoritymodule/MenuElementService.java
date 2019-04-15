package com.addplus.server.api.service.web.authoritymodule;


import com.addplus.server.api.model.authority.SysMenuElement;

import java.util.List;

/**
 * 类名: MenuElementService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/6 上午10:07
 * @description 类描述: 菜单按钮功能实现类
 */
public interface MenuElementService {

    List<String> getUserFunctionList(Integer userId) throws Exception;

    List<SysMenuElement> getMenuElementList() throws Exception;

    List<SysMenuElement> getMenuElementByfunctionId(Integer mId) throws Exception;

    SysMenuElement getMenuElementById(Long eId)throws Exception;

    Boolean addMenuElement(SysMenuElement sysMenuElement)throws Exception;

    Boolean deleteMenuElementById(Long eId)throws Exception;

    Boolean updateMenuElementById(SysMenuElement sysMenuElement)throws Exception;




}
