package me.superkoh.kframework.lib.sns.wechat.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.builder.AbstractCommonSqlBuilder;
import me.superkoh.kframework.lib.sns.wechat.model.config.properties.WxDbProperties;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
public class WxUserSqlBuilder extends AbstractCommonSqlBuilder {
    @Override
    protected String getTableName() {
        return WxDbProperties.WX_USER_TABLE;
    }

    public String selectByAppOpenId(String openId) {
        return new SQL()
                .SELECT("*")
                .FROM(this.getTableName())
                .WHERE("open_id_for_app=#{openId}").toString();
    }

    public String selectByUnionId(String unionId) {
        return new SQL()
                .SELECT("*")
                .FROM(this.getTableName())
                .WHERE("union_id=#{unionId}").toString();
    }
}
