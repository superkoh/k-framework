package me.superkoh.kframework.mvc.controller.interceptor;

import me.superkoh.kframework.core.utils.ACU;
import me.superkoh.kframework.mvc.controller.bean.RequestAttributes;
import me.superkoh.kframework.mvc.controller.config.RequestHeaderProperties;
import me.superkoh.kframework.mvc.security.LoginUser;
import me.superkoh.kframework.mvc.security.LoginUserService;
import org.cache2k.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

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
            LoginUser loginUser = null;
            Cache<String, LoginUser> localUserCache = (Cache<String, LoginUser>) ACU.bean("localUserCache");
            String localUserCacheKey = loginUserService.getClass().getCanonicalName() + ":" + token;
            if (null != localUserCache && localUserCache.containsKey(localUserCacheKey)) {
                loginUser = localUserCache.get(localUserCacheKey);
                if (loginUser.getTokenExpireTime().isBefore(LocalDateTime.now())) {
                    localUserCache.remove(localUserCacheKey);
                    loginUser = null;
                }
            }
            if (null == loginUser) {
                loginUser = loginUserService.getUserByToken(token);
                if (null != loginUser) {
                    if (loginUser.getTokenExpireTime().isBefore(LocalDateTime.now())) {
                        loginUser = null;
                    } else {
                        if (null != localUserCache) {
                            localUserCache.put(localUserCacheKey, loginUser);
                        }
                    }
                }
            }
            if (null != loginUser) {
                requestAttributes.setLoginUser(loginUser);
            }
        }
        return true;
    }
}
