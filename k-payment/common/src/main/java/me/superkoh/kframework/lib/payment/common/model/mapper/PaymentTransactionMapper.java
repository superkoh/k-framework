package me.superkoh.kframework.lib.payment.common.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.annotation.KMapper;
import me.superkoh.kframework.lib.payment.common.model.domain.PaymentTransaction;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by zhangyh on 2017/5/26.
 * 交易记录
 */
@KMapper
public interface PaymentTransactionMapper {
    @InsertProvider(type = PaymentTransactionSqlBuilder.class, method = "insert")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Integer.class)
    int insert(PaymentTransaction record);

    @UpdateProvider(type = PaymentTransactionSqlBuilder.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(PaymentTransaction record);

    @UpdateProvider(type = PaymentTransactionSqlBuilder.class, method = "updateToNeedCloseByOrderId")
    int updateToNeedCloseByOrderIdAndExceptTradeId(@Param("orderId") String orderId, @Param("tradeId") Integer tradeId);

    @UpdateProvider(type = PaymentTransactionSqlBuilder.class, method = "updateToNotPay")
    int updateToNotPay(@Param("orderId") String orderId, @Param("payMethod") String payMethod, @Param("time") Long time, @Param("status") String status);

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectByPrimaryKey")
    PaymentTransaction selectByPrimaryKey(Integer id);

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectByOrderIdAndPayMethod")
    List<PaymentTransaction> selectByOrderIdAndPayMethod(@Param("orderId") String orderId, @Param("payMethod") String payMethod);

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectNeedCloseTrades")
    List<PaymentTransaction> selectNeedClosedTransactions();

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectExpiredTrades")
    List<PaymentTransaction> selectExpiredTransactions(@Param("orderIdList") List<String> orderIdList);

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectNeedQueryTrades")
    List<PaymentTransaction> selectNeedQueryTransactions(@Param("maxTime") Long maxTime, @Param("minTime") Long minTime, @Param("status") String status);

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectNeedQueryRefundStateTransactions")
    List<PaymentTransaction> selectNeedQueryRefundStateTransactions(@Param("status") String status);

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectCanApplyRefundTransactions")
    List<PaymentTransaction> selectCanApplyRefundTransactions(@Param("status") String status,
                                                              @Param("orderIdList") List<String> orderIdList);
}
