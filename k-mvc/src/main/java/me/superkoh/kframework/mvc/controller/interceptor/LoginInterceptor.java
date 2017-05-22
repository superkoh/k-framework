package me.superkoh.kframework.mvc.controller.interceptor;

import me.superkoh.kframework.mvc.controller.annotation.GuestRequired;
import me.superkoh.kframework.mvc.controller.annotation.LoginRequired;
import me.superkoh.kframework.mvc.controller.bean.RequestAttributes;
import me.superkoh.kframework.mvc.exception.NeedGuestException;
import me.superkoh.kframework.mvc.exception.NotLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RequestAttributes requestAttributes;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (requestAttributes.getLoginUser().getId() < 1 || requestAttributes.getLoginUser().getTokenExpireTime().isBefore
                (LocalDateTime.now())) {
            if (handlerMethod.getMethod().isAnnotationPresent(LoginRequired.class)) {
                throw new NotLoginException();
            }
        } else {
            if (handlerMethod.getMethod().isAnnotationPresent(GuestRequired.class)) {
                throw new NeedGuestException();
            }
        }
        return true;
    }
}
