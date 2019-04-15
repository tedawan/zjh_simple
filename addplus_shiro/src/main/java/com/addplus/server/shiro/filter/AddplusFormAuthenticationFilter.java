package com.addplus.server.shiro.filter;

import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.model.base.ReturnDataSet;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddplusFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        ReturnDataSet returnDataSet = new ReturnDataSet();
        String loginUrl = this.getLoginUrl();
        returnDataSet.setReturnCode(loginUrl);
        httpServletResponse.setHeader(StringConstant.ACCESS_CONTROL_ALLOW_CREDENTIALS,"true");
        httpServletResponse.setHeader(StringConstant.ACCESS_CONTROL_ALLOW_ORIGIN,httpServletRequest.getHeader("Origin"));
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().print(JSON.toJSON(returnDataSet));
    }
}
