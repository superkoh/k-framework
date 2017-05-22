package me.superkoh.kframework.lib.sns.wechat.model.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
@ConfigurationProperties(prefix = WxAppProperties.WX_APP_PREFIX)
public class WxAppProperties {
    public static final String WX_APP_PREFIX = "kframework.sns.wx.app";

    private String appId;
    private String secret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
