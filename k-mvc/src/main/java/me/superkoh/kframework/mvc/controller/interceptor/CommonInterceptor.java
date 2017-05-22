package me.superkoh.kframework.mvc.controller.interceptor;

import me.superkoh.kframework.mvc.controller.bean.RequestAttributes;
import me.superkoh.kframework.mvc.controller.config.RequestHeaderProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class CommonInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RequestAttributes requestAttributes;
    @Autowired
    private RequestHeaderProperties requestHeaderProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        requestAttributes.setRemoteIp(getRemoteIpAddress(request));
        requestAttributes.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        String deviceToken = request.getHeader(requestHeaderProperties.getDeviceToken());
        if (null == deviceToken || deviceToken.isEmpty()) {
            Cookie[] cookies = request.getCookies();
            if (null != cookies && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(requestHeaderProperties.getSessionCookieName())) {
                        deviceToken = cookie.getValue();
                        break;
                    }
                }
            }
        }
        if (null == deviceToken || deviceToken.isEmpty()) {
            deviceToken = UUID.randomUUID().toString();
            response.setHeader(requestHeaderProperties.getDeviceToken(), deviceToken);
            Cookie cookie = new Cookie(requestHeaderProperties.getSessionCookieName(), deviceToken);
            response.addCookie(cookie);
        }
        requestAttributes.setDeviceToken(deviceToken);
        return true;
    }

    private String getRemoteIpAddress(HttpServletRequest request) {
        String ip = request.getHeader(requestHeaderProperties.getCustomRemoteIp());
        if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (null == ip || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
