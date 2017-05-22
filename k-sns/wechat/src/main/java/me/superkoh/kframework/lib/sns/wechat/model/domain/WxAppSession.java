package me.superkoh.kframework.lib.sns.wechat.model.domain;

import me.superkoh.kframework.lib.db.mybatis.annotation.PK;
import me.superkoh.kframework.lib.sns.wechat.app.bean.WxAppSessionRes;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
public class WxAppSession {
    @PK
    private Long id;
    private String openId;
    private String sessionKey;
    private Long expiresIn;

    public WxAppSession() {
    }

    public WxAppSession(WxAppSessionRes session) {
        this.openId = session.getOpenid();
        this.expiresIn = session.getExpiresIn();
        this.sessionKey = session.getSessionKey();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
