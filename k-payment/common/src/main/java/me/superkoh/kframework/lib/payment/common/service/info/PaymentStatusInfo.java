package me.superkoh.kframework.lib.payment.common.service.info;

import me.superkoh.kframework.lib.payment.common.type.PaymentServiceStatus;
import me.superkoh.kframework.lib.payment.common.type.PaymentStatus;

/**
 *
 * Created by zhangyh on 2016/10/11.
 */
public class PaymentStatusInfo {
    // 内部订单号
    private String tradeId;
    // 订单总价
    private String totalAmount;
    // 价格单位是否为元
    private boolean amountUnitIsYuan;
    // 付款时间
    private Long paymentTime;
    // 退款时间
    private Long refundTime;
    // 系统级服务状态，签名验证，系统参数错误等
    private PaymentServiceStatus serviceStatus;
    // 交易状态
    private PaymentStatus status;
    // 通信错误号
    private String serviceErrorCode;
    // 通信错误描述
    private String serviceErrorDesc;
    // 业务错误号
    private String errorCode;
    // 业务错误描述
    private String errorDesc;
    // 银联交易，查无此交易
    private boolean unionOrderNotExist;

    private Integer refundAmount;

    public void setTradeIdWithPrefix(String tradeId, String prefix) {
        if (null != tradeId) {
            if (tradeId.startsWith(prefix) && !prefix.isEmpty()) {
                this.tradeId = tradeId.substring(prefix.length());
            } else {
                this.tradeId = tradeId;
            }
        } else {
            this.tradeId = null;
        }
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setAmountUnitIsYuan(boolean amountUnitIsYuan) {
        this.amountUnitIsYuan = amountUnitIsYuan;
    }

    public boolean getAmountUnitIsYuan() {
        return amountUnitIsYuan;
    }

    public void setServiceStatus(PaymentServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public PaymentServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceErrorCode(String serviceErrorCode) {
        this.serviceErrorCode = serviceErrorCode;
    }

    public String getServiceErrorCode() {
        return serviceErrorCode;
    }

    public void setServiceErrorDesc(String serviceErrorDesc) {
        this.serviceErrorDesc = serviceErrorDesc;
    }

    public String getServiceErrorDesc() {
        return serviceErrorDesc;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public Long getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Long paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Long getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Long refundTime) {
        this.refundTime = refundTime;
    }

    public boolean getUnionOrderNotExist() {
        return unionOrderNotExist;
    }

    public void setUnionOrderNotExist(boolean unionOrderNotExist) {
        this.unionOrderNotExist = unionOrderNotExist;
    }

    public Integer getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }
}
