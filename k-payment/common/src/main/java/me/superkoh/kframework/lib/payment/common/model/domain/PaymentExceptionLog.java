package me.superkoh.kframework.lib.payment.common.model.domain;

import me.superkoh.kframework.lib.db.common.domain.BaseDomain;
import me.superkoh.kframework.lib.db.mybatis.annotation.PK;

/**
 * 交易异常
 * Created by zhangyh on 2017/5/26.
 */
public class PaymentExceptionLog extends BaseDomain {
    @PK
    private Integer id;
    private String orderId;
    private Integer transactionId;
    private String prevStatus;
    private String currentStatus;
    private String description;
    private Long time;

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

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getPrevStatus() {
        return prevStatus;
    }

    public void setPrevStatus(String prevStatus) {
        this.prevStatus = prevStatus;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
