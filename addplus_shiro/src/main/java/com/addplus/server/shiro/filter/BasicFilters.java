package com.addplus.server.shiro.filter;

import com.addplus.server.api.model.base.ReturnDataSet;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类名: BasicFilters
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/7 下午8:41
 * @description 类描述: shiro自定义过滤器，没有加到用户权限的接口都不能访问
 */
public class BasicFilters extends PermissionsAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        return false;
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
        String orgin = httpServletRequest.getHeader("Origin");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials","true");
        httpServletResponse.setHeader("Access-Control-Allow-Origin",orgin);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().print(JSON.toJSON(returnDataSet));

        return false;
    }
}
