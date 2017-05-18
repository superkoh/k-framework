package me.superkoh.kframework.lib.db.mybatis.test;

import me.superkoh.kframework.lib.db.mybatis.builder.AbstractCommonSqlBuilder;

/**
 * Created by KOH on 2017/5/19.
 * <p>
 * k-framework
 */
public class UserSqlBuilder extends AbstractCommonSqlBuilder {

    @Override
    protected String getTableName() {
        return "user";
    }
}
