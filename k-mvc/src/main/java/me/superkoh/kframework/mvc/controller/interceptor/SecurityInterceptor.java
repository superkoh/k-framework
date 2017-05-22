package me.superkoh.kframework.mvc.controller.interceptor;

import me.superkoh.kframework.mvc.controller.config.RequestHeaderProperties;
import me.superkoh.kframework.mvc.exception.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityInterceptor extends HandlerInterceptorAdapter {
    //    private static final String AUTH_STRING = "d157f184b7ae9ba56c8cfdbdcaf531bb";
    @Autowired
    private RequestHeaderProperties requestHeaderProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (null == requestHeaderProperties.getAuthSecret() || requestHeaderProperties.getAuthSecret().isEmpty()) {
            return true;
        }
        String auth = request.getHeader(requestHeaderProperties.getAuth());
        if (null == auth || !requestHeaderProperties.getAuthSecret().equals(auth)) {
            throw new PermissionDeniedException();
        }
        return true;
    }
}
