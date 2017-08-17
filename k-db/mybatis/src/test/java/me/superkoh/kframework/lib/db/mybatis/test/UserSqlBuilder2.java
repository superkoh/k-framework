package me.superkoh.kframework.lib.db.mybatis.test;

import me.superkoh.kframework.lib.db.mybatis.builder.BaseSqlBuilder;

/**
 * Created by KOH on 2017/5/19.
 * <p>
 * k-framework
 */
public class UserSqlBuilder2 extends BaseSqlBuilder<User2> {

    @Override
    protected String getTableName() {
        return "user";
    }
}
