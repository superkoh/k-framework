package me.superkoh.kframework.mvc.controller.interceptor;

import me.superkoh.kframework.mvc.security.LoginUser;
import me.superkoh.kframework.mvc.controller.bean.RequestAttributes;
import me.superkoh.kframework.mvc.controller.config.RequestHeaderProperties;
import me.superkoh.kframework.mvc.security.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RequestAttributes requestAttributes;
    @Autowired
    private RequestHeaderProperties requestHeaderProperties;

    private LoginUserService loginUserService;

    public UserInterceptor(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader(requestHeaderProperties.getUserToken());
        if (null != token) {
            LoginUser loginUser = loginUserService.getUserByToken(token);
            if (null != loginUser) {
                requestAttributes.setLoginUser(loginUser);
            }
        }
        return true;
    }
}
