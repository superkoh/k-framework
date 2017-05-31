package me.superkoh.kframework.lib.payment.common.model.mapper;

import me.superkoh.kframework.lib.db.mybatis.annotation.KMapper;
import me.superkoh.kframework.lib.payment.common.model.domain.PaymentTransaction;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * Created by zhangyh on 2017/5/26.
 * 交易记录
 */
@KMapper
public interface PaymentTransactionMapper {
    @InsertProvider(type = PaymentTransactionSqlBuilder.class, method = "insert")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Long.class)
    int insert(PaymentTransaction record);

    @UpdateProvider(type = PaymentTransactionSqlBuilder.class, method = "updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(PaymentTransaction record);

    @UpdateProvider(type = PaymentTransactionSqlBuilder.class, method = "updateToNeedCloseByOrderId")
    int updateToNeedCloseByOrderIdAndExceptTradeId(String orderId, Integer tradeId);

    @UpdateProvider(type = PaymentTransactionSqlBuilder.class, method = "updateToNotPay")
    int updateToNotPay(String orderId, String payMethod, Long time, String status);

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectByPrimaryKey")
    PaymentTransaction selectByPrimaryKey(Integer id);

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectByOrderIdAndPayMethod")
    List<PaymentTransaction> selectByOrderIdAndPayMethod(String orderId, String payMethod);

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectNeedCloseTrades")
    List<PaymentTransaction> selectNeedClosedTransactions();

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectExpiredTrades")
    List<PaymentTransaction> selectExpiredTransactions(List<String> orderIdList);

    @SelectProvider(type = PaymentTransactionSqlBuilder.class, method = "selectNeedQueryTrades")
    List<PaymentTransaction> selectNeedQueryTransactions(Long maxTime, Long minTime, String status);
}
