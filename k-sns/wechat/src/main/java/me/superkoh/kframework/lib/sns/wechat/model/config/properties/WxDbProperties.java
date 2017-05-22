package me.superkoh.kframework.lib.sns.wechat.model.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
@Configuration
@ConfigurationProperties(prefix = WxDbProperties.WX_DB_PREFIX)
public class WxDbProperties {
    public static final String WX_DB_PREFIX = "kframework.sns.wx.db";

    public static String WX_USER_TABLE = "sns_wx_user";
    public static String WX_APP_SESSION_TABLE = "sns_wx_app_session";

    private String userTable = "sns_wx_user";
    private String appSessionTable = "sns_wx_app_session";

    public String getUserTable() {
        return userTable;
    }

    public void setUserTable(String userTable) {
        this.userTable = userTable;
        WX_USER_TABLE = userTable;
    }

    public String getAppSessionTable() {
        return appSessionTable;
    }

    public void setAppSessionTable(String appSessionTable) {
        this.appSessionTable = appSessionTable;
        WX_APP_SESSION_TABLE = appSessionTable;
    }
}
