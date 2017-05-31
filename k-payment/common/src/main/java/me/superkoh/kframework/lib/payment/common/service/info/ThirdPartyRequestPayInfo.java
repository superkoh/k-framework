package me.superkoh.kframework.lib.payment.common.service.info;

import me.superkoh.kframework.lib.payment.common.type.PaymentChannel;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * 第三方支付需要的信息
 * Created by zhangyh on 2016/10/11.
 */
public class ThirdPartyRequestPayInfo {
    private String tradeId;
    private Integer amount;
    private Long transactionTime;
    private Long expireTime;
    private String productId;
    private String productName;
    private String productDesc;
    private PaymentChannel channel;
    private String userIp;
    private String authCode;
    private String openId;

    public ThirdPartyRequestPayInfo(PaymentOrderInfo orderInfo, PrepayRequestInfo requestInfo) {
        BeanUtils.copyProperties(orderInfo, this);
        this.tradeId = orderInfo.getTradeId().toString();
        this.amount = orderInfo.getAmount().multiply(BigDecimal.valueOf(100.0)).intValue();
        this.channel = requestInfo.getPayMethod().getChannel();

        this.userIp = requestInfo.getUserIp();
        this.authCode = requestInfo.getCode();
        this.openId = requestInfo.getOpenId();
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
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

    public void setChannel(PaymentChannel channel) {
        this.channel = channel;
    }

    public PaymentChannel getChannel() {
        return channel;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
