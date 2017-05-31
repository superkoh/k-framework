package me.superkoh.kframework.lib.payment.common.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.annotation.KMapper;
import me.superkoh.kframework.lib.payment.common.model.domain.PaymentExceptionLog;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;

/**
 * Created by zhangyh on 2017/5/26.
 * 交易异常记录
 */
@KMapper
public interface PaymentExceptionLogMapper {
    @InsertProvider(type = PaymentExceptionLogSqlBuilder.class, method = "insert")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(PaymentExceptionLog record);
}
