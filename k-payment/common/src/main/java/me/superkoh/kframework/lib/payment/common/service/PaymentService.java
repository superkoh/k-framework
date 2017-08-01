package me.superkoh.kframework.lib.payment.common.service;

import me.superkoh.kframework.core.exception.KException;
import me.superkoh.kframework.lib.payment.common.config.PaymentAccountInfoInterface;
import me.superkoh.kframework.lib.payment.common.service.info.*;

import java.util.List;
import java.util.Map;

/**
 * 支付服务统一入口
 * Created by zhangyh on 2016/10/10.
 */
public interface PaymentService {
    PaymentTransactionInfo getTransactionInfoById(String tradeId, Long userId) throws KException;

    PaymentPrepayInfo getPaymentPrepayInfoForWeb(PrepayRequestInfo reqInfo, PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentPrepayInfo getPaymentPrepayInfoForApp(PrepayRequestInfo reqInfo, PaymentAccountInfoInterface accountInfo) throws Exception;

    void fixedOrderToOfflineTransfer(String orderId, String userId) throws Exception;

    PaymentStatusInfo queryPaymentState(PaymentTransactionInfo transactionInfo, PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentStatusInfo closePaymentTransaction(PaymentTransactionInfo transactionInfo, PaymentAccountInfoInterface accountInfo) throws Exception;

    List<PaymentTransactionInfo> findTransactionsNeedQueryState();

    Map<String, List<PaymentTransactionInfo>> findExpiredTransactions();

    List<PaymentTransactionInfo> findTransactionsNeedClose();

    void cancelExpiredOrder(String orderId) throws Exception;

    // 通知处理
    PaymentNotifyProcessInfo handleUnionpayBackNotify(String encoding,
                                                      Map<String, String> notifyParams,
                                                      PaymentAccountInfoInterface accountInfo) throws Exception;
    PaymentNotifyProcessInfo handleUnionpayFrontNotify(String encoding,
                                                       Map<String, String> notifyParams,
                                                       PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentNotifyProcessInfo handleWxpayBackNotify(String notifyString,
                                                   PaymentAccountInfoInterface accountInfo) throws Exception;
    PaymentNotifyProcessInfo handleWxpayFrontNotify(String notifyString,
                                                    PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentNotifyProcessInfo handleAlipayBackNotify(Map<String, String> notifyParams,
                                                    PaymentAccountInfoInterface accountInfo) throws Exception;
    PaymentNotifyProcessInfo handleAlipayFrontNotify(Map<String, String> notifyParams,
                                                     PaymentAccountInfoInterface accountInfo) throws Exception;
}
