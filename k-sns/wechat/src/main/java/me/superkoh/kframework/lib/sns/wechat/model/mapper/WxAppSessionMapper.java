package me.superkoh.kframework.lib.sns.wechat.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.annotation.Mapper;
import me.superkoh.kframework.lib.sns.wechat.model.domain.WxAppSession;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
@Mapper
public interface WxAppSessionMapper {
    @InsertProvider(type = WxAppSessionSqlBuilder.class, method = "insert")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(WxAppSession record);
}
