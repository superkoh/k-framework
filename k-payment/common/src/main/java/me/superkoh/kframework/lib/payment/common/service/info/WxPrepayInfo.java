package me.superkoh.kframework.lib.payment.common.service.info;

/**
 * Created by zhangyh on 16/9/18.
 */
public class WxPrepayInfo {
    private String timestamp;
    private String nonceStr;
    private String packageStr;
    private String signType;
    private String paySign;

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSignType() {
        return signType;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getPaySign() {
        return paySign;
    }
}
