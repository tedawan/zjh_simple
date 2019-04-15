package com.addplus.server.shiro.action;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.model.authority.ext.SysLoginUser;
import com.addplus.server.api.model.base.ReturnDataSet;
import com.addplus.server.shiro.service.SystemAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 类名：BaseSystemController
 *
 * @author zhangjiehang
 * @version V1.0
 * @date  2017/10/22 19:01
 * @describe 类描述：用于初始登录未授权等方法调用
 */
@RestController
@RequestMapping("/base")
public class BaseSystemAction {

    @Autowired
    private SystemAdminService systemService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public void loginUrlAdminPost(@RequestBody SysLoginUser user) throws Exception {
        systemService.adminLogin(user, request);
    }

    @GetMapping(value = "/logout")
    public void logout() throws Exception {
        systemService.userLoginOut();
    }

    @GetMapping(value = "verify")
    public ReturnDataSet verify() throws Exception {
        ReturnDataSet returnDataSet = new ReturnDataSet();
        returnDataSet.setErrorCode(ErrorCode.SYS_SUCCESS);
        returnDataSet.setDataSet(systemService.getVerify());
        return returnDataSet;
    }


}
