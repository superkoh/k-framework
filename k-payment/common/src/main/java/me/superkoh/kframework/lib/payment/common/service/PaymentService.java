package me.superkoh.kframework.lib.payment.common.service;

import me.superkoh.kframework.core.exception.KException;
import me.superkoh.kframework.lib.payment.common.config.PaymentAccountInfo;
import me.superkoh.kframework.lib.payment.common.service.info.*;

import java.util.List;
import java.util.Map;

/**
 * 支付服务统一入口
 * Created by zhangyh on 2016/10/10.
 */
public interface PaymentService {
    PaymentTransactionInfo getTransactionInfoById(String tradeId, Long userId) throws KException;

    PaymentPrepayInfo getPaymentPrepayInfo(PrepayRequestInfo requestInfo, PaymentAccountInfo accountInfo) throws Exception;

    PaymentStatusInfo queryPaymentState(PaymentTransactionInfo transactionInfo, PaymentAccountInfo accountInfo) throws Exception;

    PaymentStatusInfo closePaymentTransaction(PaymentTransactionInfo transactionInfo, PaymentAccountInfo accountInfo) throws Exception;

    List<PaymentTransactionInfo> findTransactionsNeedQueryState();

    Map<String, List<PaymentTransactionInfo>> findExpiredTransactions();

    List<PaymentTransactionInfo> findTransactionsNeedClose();

    void cancelExpiredOrder(String orderId) throws KException;

    // 通知处理
    PaymentNotifyProcessInfo handleUnionpayBackNotify(String encoding,
                                                      Map<String, String> notifyParams,
                                                      PaymentAccountInfo accountInfo) throws Exception;
    PaymentNotifyProcessInfo handleUnionpayFrontNotify(String encoding,
                                                       Map<String, String> notifyParams,
                                                       PaymentAccountInfo accountInfo) throws Exception;

    PaymentNotifyProcessInfo handleWxpayBackNotify(String notifyString,
                                                   PaymentAccountInfo accountInfo) throws Exception;
    PaymentNotifyProcessInfo handleWxpayFrontNotify(String notifyString,
                                                    PaymentAccountInfo accountInfo) throws Exception;

    PaymentNotifyProcessInfo handleAlipayBackNotify(Map<String, String> notifyParams,
                                                    PaymentAccountInfo accountInfo) throws Exception;
    PaymentNotifyProcessInfo handleAlipayFrontNotify(Map<String, String> notifyParams,
                                                     PaymentAccountInfo accountInfo) throws Exception;
}
