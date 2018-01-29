package me.superkoh.kframework.lib.payment.common.service.info;

import me.superkoh.kframework.lib.payment.common.model.domain.PaymentTransaction;
import me.superkoh.kframework.lib.payment.common.type.PaymentMethod;
import me.superkoh.kframework.lib.payment.common.type.PaymentStatus;
import org.springframework.beans.BeanUtils;

import java.time.ZoneId;

/**
 * Created by zhangyh on 2016/10/10.
 */
public class PaymentTransactionInfo {
    private Integer id;
    private String orderId;
    private Integer wechatId;
    private String thirdTradeId;
    private PaymentMethod paymentMethod;
    private String currency;
    private String paymentTime;
    private String paymentNote;
    private Integer amount;
    private Integer couponAmount;
    private Long effectTime;
    private Long transactionTime;
    private Long expireTime;
    private ZoneId timezone;
    private PaymentStatus status;
    private Integer refundAmount;

    public PaymentTransactionInfo(PaymentTransaction transactionPO) {
        BeanUtils.copyProperties(transactionPO, this);
        this.paymentMethod = PaymentMethod.valueOf(transactionPO.getPaymentMethod());
        this.status = PaymentStatus.valueOf(transactionPO.getStatus());
        this.timezone = ZoneId.of(transactionPO.getTimezone());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Integer getWechatId() {
        return wechatId;
    }

    public void setWechatId(Integer wechatId) {
        this.wechatId = wechatId;
    }

    public String getThirdTradeId() {
        return thirdTradeId;
    }

    public void setThirdTradeId(String thirdTradeId) {
        this.thirdTradeId = thirdTradeId;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
    }

    public String getPaymentNote() {
        return paymentNote;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setCouponAmount(Integer couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Integer getCouponAmount() {
        return couponAmount;
    }

    public void setEffectTime(Long effectTime) {
        this.effectTime = effectTime;
    }

    public Long getEffectTime() {
        return effectTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setTransactionTime(Long transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Long getTransactionTime() {
        return transactionTime;
    }

    public void setTimezone(ZoneId timezone) {
        this.timezone = timezone;
    }

    public ZoneId getTimezone() {
        return timezone;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Integer getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }
}
