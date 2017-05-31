package me.superkoh.kframework.lib.sns.wechat.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.annotation.KMapper;
import me.superkoh.kframework.lib.db.mybatis.builder.SqlBuilderConstant;
import me.superkoh.kframework.lib.sns.wechat.model.domain.WxUser;
import org.apache.ibatis.annotations.*;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
@KMapper
public interface WxUserMapper {
    @InsertProvider(type = WxUserSqlBuilder.class, method = SqlBuilderConstant.INSERT)
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(WxUser record);

    @UpdateProvider(type = WxUserSqlBuilder.class, method = SqlBuilderConstant.UPDATE_BY_PRIMARY_KEY_SELECTIVE)
    int updateByPrimaryKeySelective(WxUser record);

    @SelectProvider(type = WxUserSqlBuilder.class, method = "selectByAppOpenId")
    WxUser selectByAppOpenId(String openId);

    @SelectProvider(type = WxUserSqlBuilder.class, method = "selectByOpenOpenId")
    WxUser selectByOpenOpenId(String openId);

    @SelectProvider(type = WxUserSqlBuilder.class, method = "selectByMpOpenId")
    WxUser selectByMpOpenId(String openId);

    @SelectProvider(type = WxUserSqlBuilder.class, method = "selectByUnionId")
    WxUser selectByUnionId(String unionId);
}
