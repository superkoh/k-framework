package me.superkoh.kframework.lib.payment.common.type;

/**
 * 交易状态
 * Created by zhangyh on 2016/10/11.
 */
public enum PaymentStatus {
    SUCCESS,
    NOT_PAY,
    CLOSED,
    PAY_ERROR,
    APPLIED_REFUND,
    REFUNDED,
    REFUND_FAILED
}
