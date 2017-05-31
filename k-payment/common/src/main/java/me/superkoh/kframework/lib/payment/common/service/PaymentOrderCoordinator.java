package me.superkoh.kframework.lib.payment.common.service;

import me.superkoh.kframework.core.exception.KException;
import me.superkoh.kframework.lib.payment.common.model.domain.PaymentTransaction;
import me.superkoh.kframework.lib.payment.common.service.info.PaymentOrderInfo;

import java.util.List;

/**
 * 订单，支付适配器
 * Created by zhangyh on 2017/5/31.
 */
public interface PaymentOrderCoordinator {
    PaymentOrderInfo getPaymentOrderInfoById(String orderId, String userId) throws KException;

    List<String> getExpiredOrderIdList();

    void cancelOrder(String orderId) throws KException;

    void paymentSuccess(String orderId, PaymentTransaction transactionPO) throws KException;

    void fixOrderToOfflineTransfer(String orderId) throws KException;
}
