package me.superkoh.kframework.lib.payment.common.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.builder.AbstractCommonSqlBuilder;
import me.superkoh.kframework.lib.payment.common.model.config.PaymentDbProperties;

/**
 * Log记录sql builder
 * Created by zhangyh on 2017/5/26.
 */
public class PaymentExceptionLogSqlBuilder extends AbstractCommonSqlBuilder {
    @Override
    protected String getTableName() {
        return PaymentDbProperties.PAYMENT_EXCEPTION_TABLE;
    }
}
