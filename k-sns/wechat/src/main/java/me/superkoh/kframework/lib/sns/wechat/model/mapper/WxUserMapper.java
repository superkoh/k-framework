package me.superkoh.kframework.lib.sns.wechat.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.annotation.Mapper;
import me.superkoh.kframework.lib.sns.wechat.model.domain.WxUser;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
@Mapper
public interface WxUserMapper {
    @InsertProvider(type = WxUserSqlBuilder.class, method = "insert")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(WxUser record);

    @UpdateProvider(type = WxUserSqlBuilder.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(WxUser record);



    @Select("select * from sns_wx_user where open_id_for_app=#{openId}")
    WxUser selectByAppOpenId(String openId);

    @Select("select * from sns_wx_user where union_id=#{unionId}")
    WxUser selectByUnionId(String unionId);
}
