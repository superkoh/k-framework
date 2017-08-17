package me.superkoh.kframework.lib.db.mybatis.test;

import me.superkoh.kframework.core.type.Page;
import me.superkoh.kframework.lib.db.mybatis.annotation.KMapper;
import me.superkoh.kframework.lib.db.mybatis.builder.Example;
import org.apache.ibatis.annotations.*;

import java.util.List;

import static me.superkoh.kframework.lib.db.mybatis.builder.SqlBuilderConstant.*;

/**
 * Created by KOH on 2017/5/19.
 * <p>
 * k-framework
 */
@KMapper
public interface UserMapper2 {

    @Select("select * from user where id=#{id}")
    User2 selectById(long id);

    @InsertProvider(type = UserSqlBuilder2.class, method = INSERT)
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(User2 record);

    @UpdateProvider(type = UserSqlBuilder2.class, method = UPDATE_BY_PRIMARY_KEY_SELECTIVE)
    int updateByPrimaryKeySelective(User2 record);

    @Select("<script>\n" +
            "select * from user\n" +
            WHERE_SEGMENT +
            "</script>")
    List<User2> selectByExample(@Param("example") Example example);

    @SelectProvider(type = UserSqlBuilder2.class, method = SELECT_BY_QUERY)
    List<User2> selectByQuery(@Param("query") UserQuery query, String orderBy);

    @SelectProvider(type = UserSqlBuilder2.class, method = SELECT_PAGE_BY_QUERY)
    List<User2> selectPageByQuery(@Param("query") UserQuery query, Page page);

    @SelectProvider(type = UserSqlBuilder2.class, method = COUNT_BY_QUERY)
    long countByQuery(@Param("query") UserQuery query);
}
