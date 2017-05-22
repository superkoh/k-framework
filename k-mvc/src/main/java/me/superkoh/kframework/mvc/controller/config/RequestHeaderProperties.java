package me.superkoh.kframework.mvc.controller.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by KOH on 2017/5/17.
 * <p>
 * kmvc
 */
@Configuration
@ConfigurationProperties("kframework.mvc.request.header")
public class RequestHeaderProperties {
    private String deviceToken = "X-Kmvc-Device-Token";
    private String auth = "X-Kmvc-Auth";
    private String authSecret = "";
    private String userToken = "X-Kmvc-User-Token";
    private String customRemoteIp = "k-remote-ip";
    private String sessionCookieName = "ksid";

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getSessionCookieName() {
        return sessionCookieName;
    }

    public void setSessionCookieName(String sessionCookieName) {
        this.sessionCookieName = sessionCookieName;
    }

    public String getAuthSecret() {
        return authSecret;
    }

    public void setAuthSecret(String authSecret) {
        this.authSecret = authSecret;
    }

    public String getCustomRemoteIp() {
        return customRemoteIp;
    }

    public void setCustomRemoteIp(String customRemoteIp) {
        this.customRemoteIp = customRemoteIp;
    }
}
