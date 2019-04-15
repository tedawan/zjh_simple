package com.addplus.server.shiro.config.sessioncluster;

import com.addplus.server.api.constant.StringConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 类名: ShiroSessionManager
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/1/14 下午3:49
 * @description 类描述: 优化原有的SessionManager多次调用redis问题
 */
public class ShiroSessionManager extends DefaultWebSessionManager {

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);

        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }

        if (request != null && null != sessionId) {
            Object sessionObj = request.getAttribute(sessionId.toString());
            if (sessionObj != null) {
                return (Session) sessionObj;
            }
        }

        Session session = super.retrieveSession(sessionKey);
        if (request != null && null != sessionId) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // 从请求头中获取token
        String token = extractHeaderToken(request);
        if (StringUtils.isBlank(token)) {
            token = extractToken(request);
        }
        if (StringUtils.isNoneBlank(token)) {
            // 设置当前session状态
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "url");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return token;
        }
        // 若header获取不到token则尝试从cookie中获取
        return super.getSessionId(request, response);
    }

    private String extractToken(ServletRequest request) {
        String token = extractHeaderToken(request);
        if (StringUtils.isBlank(token)) {
            // 从参数中取token
            token = WebUtils.toHttp(request).getParameter(StringConstant.TOKEN);
        }
        return token;
    }

    private String extractHeaderToken(ServletRequest request) {
        // 从请求头中获取token
        String tokenExtract = WebUtils.toHttp(request).getHeader(StringConstant.ACCESS_CONTROL_ALLOW_AUTHORIZATION);
        if (StringUtils.isNoneBlank(tokenExtract)) {
            int index = tokenExtract.indexOf(StringConstant.TOKEN, 0);
            if (index != -1 && index == 0) {
                index = index + StringConstant.TOKEN.length() + 1;
                if (tokenExtract.length() > index) {
                    return tokenExtract.substring(index);
                }
            }
        }
        return null;
    }
}
