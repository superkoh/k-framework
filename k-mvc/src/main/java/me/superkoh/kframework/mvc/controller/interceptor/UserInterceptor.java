package me.superkoh.kframework.mvc.controller.interceptor;

import me.superkoh.kframework.mvc.controller.bean.RequestAttributes;
import me.superkoh.kframework.mvc.controller.config.RequestHeaderProperties;
import me.superkoh.kframework.mvc.security.LoginUser;
import me.superkoh.kframework.mvc.security.LoginUserService;
import org.cache2k.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Autowired(required = false)
    @Qualifier("localUserCache")
    private Cache<String, LoginUser> localUserCache;

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
            String localUserCacheKey = loginUserService.getClass().getCanonicalName() + ":" + token;
            LoginUser loginUser = null;
            if (null != localUserCache && localUserCache.containsKey(localUserCacheKey)) {
                loginUser = localUserCache.get(localUserCacheKey);
                if (loginUser.getTokenExpireTime().isAfter(LocalDateTime.now())) {
                    localUserCache.remove(localUserCacheKey);
                    loginUser = null;
                }
            }
            if (null == loginUser) {
                loginUser = loginUserService.getUserByToken(token);
                if (null != loginUser) {
                    if (loginUser.getTokenExpireTime().isAfter(LocalDateTime.now())) {
                        loginUser = null;
                    } else {
                        localUserCache.put(localUserCacheKey, loginUser);
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
