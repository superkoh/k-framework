package me.superkoh.kframework.lib.payment.common.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.annotation.KMapper;
import me.superkoh.kframework.lib.payment.common.model.domain.PaymentTransaction;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
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

    @Update("UPDATE payment_transaction SET amount=#{amount} WHERE order_id=#{orderId} AND (status!='NOT_PAY' OR status!='PAY_ERROR')")
    int updateAmountByOrderId(@Param("orderId") String orderId, @Param("amount") int amount);

    @Update("UPDATE payment_transaction SET refund_amount=#{amount} WHERE order_id=#{orderId}")
    int updateRefundAmountByOrderId(@Param("orderId") String orderId, @Param("amount") int amount);

    @Update("UPDATE payment_transaction SET need_close=1, status='PAY_ERROR' WHERE order_id=#{orderId} AND (status!='SUCCESS' AND status!='CLOSED')")
    int updateToNeedCloseAndPayError(@Param("orderId") String orderId);

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

    @Select("SELECT * FROM payment_transaction WHERE status=#{status} ORDER BY id ASC LIMIT 200")
    List<PaymentTransaction> selectNeedQueryRefundStateTransactions(String status);

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectCanApplyRefundTransactions")
    List<PaymentTransaction> selectCanApplyRefundTransactions(
            @Param("status") String status, @Param("orderIdList") List<String> orderIdList);
}
