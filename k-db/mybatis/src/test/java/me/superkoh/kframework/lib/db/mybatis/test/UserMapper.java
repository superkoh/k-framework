package me.superkoh.kframework.lib.db.mybatis.test;

import me.superkoh.kframework.core.type.Page;
import me.superkoh.kframework.lib.db.mybatis.annotation.KMapper;
import me.superkoh.kframework.lib.db.mybatis.builder.Example;
import me.superkoh.kframework.lib.db.mybatis.builder.SqlBuilderConstant;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by KOH on 2017/5/19.
 * <p>
 * k-framework
 */
@KMapper
public interface UserMapper {

    @Select("select * from user where id=#{id}")
    User selectById(long id);

    @InsertProvider(type = UserSqlBuilder.class, method = "insert")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(User record);

    @UpdateProvider(type = UserSqlBuilder.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(User record);

    @Select("<script>\n" +
            "select * from user\n" +
            SqlBuilderConstant.WHERE_SEGMENT +
            "</script>")
    List<User> selectByExample(@Param("example") Example example);

    @SelectProvider(type = UserSqlBuilder.class, method = "selectByQuery")
    List<User> selectByQuery(Class clazz, @Param("query") UserQuery query, String orderBy);

    @SelectProvider(type = UserSqlBuilder.class, method = "selectPageByQuery")
    List<User> selectPageByQuery(Class clazz, @Param("query") UserQuery query, Page page);

    @SelectProvider(type = UserSqlBuilder.class, method = "countByQuery")
    long countByQuery(@Param("query") UserQuery query);
}
