package me.superkoh.kframework.mvc.controller.interceptor;

import me.superkoh.kframework.mvc.controller.config.ResponseHeaderProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by KOH on 2017/4/4.
 * <p>
 * webFramework
 */
public class CorsInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private ResponseHeaderProperties responseHeaderProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String origin = request.getHeader(HttpHeaders.ORIGIN);
        if (null == origin || origin.isEmpty()) {
            return true;
        }
        if (responseHeaderProperties.getAccessControlAllowOrigins().contains(origin.trim())) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        } else {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                    responseHeaderProperties.getAccessControlAllowOrigins().get(0));
        }
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                StringUtils.join(responseHeaderProperties.getAccessControlAllowMethods(), ","));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE,
                String.valueOf(responseHeaderProperties.getAccessControlMaxAge()));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                StringUtils.join(responseHeaderProperties.getAccessControlAllowHeaders(), ","));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                String.valueOf(responseHeaderProperties.getAccessControlAllowCredentials()));
        return true;
    }
}
