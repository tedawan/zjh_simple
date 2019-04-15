package com.addplus.server.shiro.filter;

import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.model.base.ReturnDataSet;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddplusPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Subject subject = this.getSubject(request, response);
        ReturnDataSet returnDataSet = new ReturnDataSet();
        if (subject.getPrincipal() == null) {
            String loginUrl = this.getLoginUrl();
            returnDataSet.setReturnCode(loginUrl);
        } else {
            String unauthorizedUrl = this.getUnauthorizedUrl();
            returnDataSet.setReturnCode(unauthorizedUrl);
        }
        httpServletResponse.setHeader(StringConstant.ACCESS_CONTROL_ALLOW_CREDENTIALS,"true");
        httpServletResponse.setHeader(StringConstant.ACCESS_CONTROL_ALLOW_ORIGIN,httpServletRequest.getHeader("Origin"));
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().print(JSON.toJSON(returnDataSet));
        return false;
    }


}
