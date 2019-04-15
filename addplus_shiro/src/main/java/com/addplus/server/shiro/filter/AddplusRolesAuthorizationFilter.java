package com.addplus.server.shiro.filter;

import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.model.base.ReturnDataSet;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class AddplusRolesAuthorizationFilter extends RolesAuthorizationFilter {


    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = this.getSubject(request, response);
        String[] rolesArray = (String[])((String[])mappedValue);
        if (rolesArray != null && rolesArray.length != 0) {
            Set<String> roles = CollectionUtils.asSet(rolesArray);
            for(String role : roles) {
                if(subject.hasRole(role)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

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
