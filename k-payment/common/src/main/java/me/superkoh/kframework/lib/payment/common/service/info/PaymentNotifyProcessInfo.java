package me.superkoh.kframework.lib.payment.common.service.info;

/**
 * 支付通知信息
 * Created by zhangyh on 2016/10/12.
 */
public class PaymentNotifyProcessInfo {

    private PaymentStatusInfo statusInfo;
    private String responseBody;
    private String responseEncoding;
    private String responseContentType;

    public PaymentNotifyProcessInfo() {
        this.statusInfo = new PaymentStatusInfo();
    }

    public void setStatusInfo(PaymentStatusInfo statusInfo) {
        this.statusInfo = statusInfo;
    }

    public PaymentStatusInfo getStatusInfo() {
        return statusInfo;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseEncoding(String responseEncoding) {
        this.responseEncoding = responseEncoding;
    }

    public String getResponseEncoding() {
        return responseEncoding;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public String getResponseContentType() {
        return responseContentType;
    }
}
