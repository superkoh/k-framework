package me.superkoh.kframework.lib.payment.common.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.builder.AbstractCommonSqlBuilder;
import me.superkoh.kframework.lib.payment.common.model.config.PaymentDbProperties;
import org.apache.ibatis.jdbc.SQL;

/**
 * 交易sql builder
 * Created by zhangyh on 2017/5/26.
 */
public class PaymentTransactionSqlBuilder extends AbstractCommonSqlBuilder {
    @Override
    protected String getTableName() {
        return PaymentDbProperties.PAYMENT_TRADE_TABLE;
    }

    public String updateToNeedCloseByOrderId() {
        return new SQL()
                .UPDATE(getTableName())
                .SET("need_close=1")
                .WHERE("order_id=#{orderId}")
                .WHERE("id!=#{tradeId}").toString();
    }

    public String updateToNotPay() {
        return new SQL()
                .UPDATE(getTableName())
                .SET("status=#{status}")
                .SET("transaction_time=#{time}")
                .WHERE("order_id=#{orderId}")
                .WHERE("payment_method=#{payMethod}")
                .WHERE("status!=SUCCESS")
                .WHERE("status!=CLOSED").toString();
    }

    public String selectByPrimaryKey() {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("id=#{id}").toString();
    }

    public String selectByOrderIdAndPayMethod() {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("order_id=#{orderId}")
                .WHERE("payment_method=#{paymentMethod}").toString();
    }

    public String selectNeedCloseTrades() {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("need_close=1")
                .WHERE("status!=CLOSED")
                .WHERE("status!=SUCCESS").toString();
    }

    public String selectExpiredTrades() {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("order_id IN #{orderIdList}").toString();
    }

    public String selectNeedQueryTrades() {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("transaction_time <= #{maxTime}")
                .WHERE("expire_time >= #{minTime}")
                .WHERE("status = #{status}")
                .ORDER_BY("transaction_time ASC LIMIT 200").toString();
    }
}
