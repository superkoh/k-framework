package me.superkoh.kframework.lib.db.mybatis.test;

import me.superkoh.kframework.lib.db.mybatis.annotation.Mapper;
import me.superkoh.kframework.lib.db.mybatis.builder.AbstractCommonSqlBuilderTest;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;

/**
 * Created by KOH on 2017/5/19.
 * <p>
 * k-framework
 */
@Mapper
public interface UserMapper {
    @InsertProvider(type = AbstractCommonSqlBuilderTest.UserSqlBuilder.class, method = "insert")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(AbstractCommonSqlBuilderTest.User record);
}
