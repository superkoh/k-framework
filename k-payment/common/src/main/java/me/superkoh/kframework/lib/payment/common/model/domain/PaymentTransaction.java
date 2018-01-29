package me.superkoh.kframework.lib.payment.common.model.domain;

import me.superkoh.kframework.lib.db.common.domain.TimeAndAuthorTraceableDomain;
import me.superkoh.kframework.lib.db.mybatis.annotation.Column;
import me.superkoh.kframework.lib.db.mybatis.annotation.PK;
import me.superkoh.kframework.lib.payment.common.type.PaymentStatus;
import org.apache.ibatis.type.JdbcType;

/**
 * 交易结构
 * Created by zhangyh on 2017/5/26.
 */
public class PaymentTransaction extends TimeAndAuthorTraceableDomain {
    @PK
    private Integer id;
    private String orderId;
    private String paymentMethod;
    private String currency;
    private Integer amount;
    private Integer couponAmount;
    private Integer refundAmount;
    private Long paymentTime;
    private String paymentNote;
    private Long effectTime;
    private Long transactionTime;
    private Long expireTime;
    private Long refundTime;
    private String timezone;
    private String status;
    private Boolean needClose;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Integer couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Long getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Long paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentNote() {
        return paymentNote;
    }

    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
    }

    public Long getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(Long effectTime) {
        this.effectTime = effectTime;
    }

    public Long getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Long transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Long getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Long refundTime) {
        this.refundTime = refundTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getNeedClose() {
        return needClose;
    }

    public void setNeedClose(Boolean needClose) {
        this.needClose = needClose;
    }

    public Integer getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }
}
