package me.superkoh.kframework.lib.payment.common.service;

import me.superkoh.kframework.lib.payment.common.model.domain.PaymentTransaction;
import me.superkoh.kframework.lib.payment.common.service.info.PaymentOrderInfo;

import java.util.List;

/**
 * 订单，支付适配器
 * Created by zhangyh on 2017/5/31.
 */
public interface PaymentOrderCoordinator {
    PaymentOrderInfo getPaymentOrderInfoById(String orderId, String userId) throws Exception;

    List<String> getExpiredOrderIdList();

    void cancelOrder(String orderId) throws Exception;

    void paymentSuccess(String orderId, PaymentTransaction transactionPO) throws Exception;

    void fixOrderToOfflineTransfer(String orderId) throws Exception;

    List<String> getUserAppliedRefundOrderIdList();

    void updateRefundStatus(String orderId, PaymentTransaction transactionPO) throws Exception;
}
