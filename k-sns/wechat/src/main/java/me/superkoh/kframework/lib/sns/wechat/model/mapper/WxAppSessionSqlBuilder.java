package me.superkoh.kframework.lib.sns.wechat.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.builder.AbstractCommonSqlBuilder;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
public class WxAppSessionSqlBuilder extends AbstractCommonSqlBuilder {
    @Override
    protected String getTableName() {
        return "sns_wx_app_session";
    }
}
