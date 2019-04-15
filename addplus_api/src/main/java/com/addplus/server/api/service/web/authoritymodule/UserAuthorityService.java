package com.addplus.server.api.service.web.authoritymodule;


import com.addplus.server.api.model.authority.SysMenuElement;
import com.addplus.server.api.model.authority.ext.SysMenuFunctionUser;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 类名: UserAuthorityService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/4 下午4:25
 * @description 类描述: 用户权限业务接口类
 */
public interface UserAuthorityService {
    List<SysMenuFunctionUser> getUserMenuFunctionByUserId() throws Exception;

    List<SysMenuElement> getUserMenuElemnetList(Integer mId) throws Exception;

    JSONObject getByUser() throws Exception;

}
