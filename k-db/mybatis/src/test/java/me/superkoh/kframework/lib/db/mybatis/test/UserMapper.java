package me.superkoh.kframework.lib.db.mybatis.test;

import me.superkoh.kframework.lib.db.mybatis.annotation.Mapper;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * Created by KOH on 2017/5/19.
 * <p>
 * k-framework
 */
@Mapper
public interface UserMapper {

    @Select("select * from user where id=#{id}")
    User selectById(long id);

    @InsertProvider(type = UserSqlBuilder.class, method = "insert")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(User record);

    @UpdateProvider(type = UserSqlBuilder.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(User record);
}
