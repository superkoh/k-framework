package me.superkoh.kframework.lib.payment.common.service.info;

import me.superkoh.kframework.lib.payment.common.type.PaymentMethod;
import org.springframework.beans.BeanUtils;

/**
 * Created by zhangyh on 2016/10/14.
 */
public class PrepayRequestInfo {
    private String orderId;
    private PaymentMethod payMethod;
    private String code;
    private String openId;
    private String userIp;
    private String userId;

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setPayMethod(PaymentMethod payMethod) {
        this.payMethod = payMethod;
    }

    public PaymentMethod getPayMethod() {
        return payMethod;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getUserIp() {
        return userIp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
