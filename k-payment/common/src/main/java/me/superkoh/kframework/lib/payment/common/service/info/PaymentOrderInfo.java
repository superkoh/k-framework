package me.superkoh.kframework.lib.payment.common.service.info;

import java.math.BigDecimal;

/**
 * 支付系统需要的订单信息
 * Created by zhangyh on 2016/10/10.
 */
public class PaymentOrderInfo {
    private Integer wechatId;
    private Integer tradeId;
    private Integer orderId;
    private Integer userId;
    private BigDecimal singlePrice;
    private BigDecimal amount;
    private BigDecimal couponAmount;
    private String currency;
    private Integer totalQuantity;
    private Long confirmTime;
    private String productId;
    private String productName;
    private String productDesc;
    private Long transactionTime;
    private Long expireTime;
    private Long effectTime;
    private Boolean offlineTransfer;

    public Integer getWechatId() {
        return wechatId;
    }

    public void setWechatId(Integer wechatId) {
        this.wechatId = wechatId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setSinglePrice(BigDecimal singlePrice) {
        this.singlePrice = singlePrice;
    }

    public BigDecimal getSinglePrice() {
        return singlePrice;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setConfirmTime(Long confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Long getConfirmTime() {
        return confirmTime;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setTransactionTime(Long transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Long getTransactionTime() {
        return transactionTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setEffectTime(Long effectTime) {
        this.effectTime = effectTime;
    }

    public Long getEffectTime() {
        return effectTime;
    }

    public Boolean getOfflineTransfer() {
        return offlineTransfer;
    }

    public void setOfflineTransfer(Boolean offlineTransfer) {
        this.offlineTransfer = offlineTransfer;
    }
}
