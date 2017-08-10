package me.superkoh.kframework.lib.payment.common.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.builder.AbstractCommonSqlBuilder;
import me.superkoh.kframework.lib.payment.common.model.config.PaymentDbProperties;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * 交易sql builder
 * Created by zhangyh on 2017/5/26.
 */
public class PaymentTransactionSqlBuilder extends AbstractCommonSqlBuilder {
    @Override
    protected String getTableName() {
        return PaymentDbProperties.PAYMENT_TRADE_TABLE;
    }

    public String updateToNeedCloseByOrderId(@Param("orderId") String orderId, @Param("tradeId") Integer tradeId) {
        return new SQL()
                .UPDATE(getTableName())
                .SET("need_close=1")
                .WHERE("order_id=#{orderId}")
                .WHERE("id!=#{tradeId}").toString();
    }

    public String updateToNotPay(@Param("orderId") String orderId, @Param("payMethod") String payMethod, @Param("time") Long time, @Param("status") String status) {
        return new SQL()
                .UPDATE(getTableName())
                .SET("status=#{status}")
                .SET("transaction_time=#{time}")
                .WHERE("order_id=#{orderId}")
                .WHERE("payment_method=#{payMethod}")
                .WHERE("status!='SUCCESS'")
                .WHERE("status!='CLOSED'").toString();
    }

    public String selectByPrimaryKey(Integer id) {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("id=#{id}").toString();
    }

    public String selectByOrderIdAndPayMethod(@Param("orderId") String orderId, @Param("payMethod") String payMethod) {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("order_id=#{orderId}")
                .WHERE("payment_method=#{payMethod}").toString();
    }

    public String selectNeedCloseTrades() {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("need_close=1")
                .WHERE("status!='CLOSED'")
                .WHERE("status!='SUCCESS'").toString();
    }

    public String selectExpiredTrades(List<String> orderIdList) {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("order_id IN #{orderIdList}").toString();
    }

    public String selectNeedQueryTrades(@Param("maxTime") Long maxTime, @Param("minTime") Long minTime, @Param("status") String status) {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("transaction_time <= #{maxTime}")
                .WHERE("expire_time >= #{minTime}")
                .WHERE("status = #{status}")
                .ORDER_BY("transaction_time ASC LIMIT 200").toString();
    }

    public String selectNeedQueryRefundStateTransactions(@Param("status") String status) {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("status = #{status}")
                .ORDER_BY("id ASC LIMIT 200").toString();
    }

    public String selectCanApplyRefundTransactions(@Param("status") String status, @Param("orderIdList") List<Integer> orderIdList) {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE("status = #{status}")
                .WHERE("order_id IN <foreach close=\")\" collection=\"orderIdList\" item=\"listItem\" open=\"(\" separator=\",\">\n #{listItem,jdbcType=INTEGER}\n </foreach>")
                .ORDER_BY("id ASC LIMIT 200").toString();
    }
}
