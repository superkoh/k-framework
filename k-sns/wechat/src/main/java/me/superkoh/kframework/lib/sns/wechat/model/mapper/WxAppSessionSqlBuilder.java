package me.superkoh.kframework.lib.sns.wechat.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.builder.AbstractCommonSqlBuilder;
import me.superkoh.kframework.lib.sns.wechat.model.config.properties.WxDbProperties;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
public class WxAppSessionSqlBuilder extends AbstractCommonSqlBuilder {
    @Override
    protected String getTableName() {
        return WxDbProperties.WX_APP_SESSION_TABLE;
    }
}
