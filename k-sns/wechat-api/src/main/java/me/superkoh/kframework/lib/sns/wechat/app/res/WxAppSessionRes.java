package me.superkoh.kframework.lib.sns.wechat.app.res;

/**
 * Created by KOH on 2017/4/25.
 * <p>
 * webFramework
 */
public class WxAppSessionRes {
    private Long expiresIn;
    private String openid;
    private String sessionKey;

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
