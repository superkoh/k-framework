package me.superkoh.kframework.lib.payment.common.service.info;

/**
 * Created by zhangyh on 2016/10/11.
 */
public class PaymentPrepayInfo {
    private Integer wechatId;
    private UnionPrepayInfo unionpay;
    private WxPrepayInfo wxJsApiPrepay;
    private String wxNativeCodeUrl;
    private AlipayPrepayInfo alipay;

    public Integer getWechatId() {
        return wechatId;
    }

    public void setWechatId(Integer wechatId) {
        this.wechatId = wechatId;
    }

    public void setUnionpay(UnionPrepayInfo unionpay) {
        this.unionpay = unionpay;
    }

    public UnionPrepayInfo getUnionpay() {
        return unionpay;
    }

    public void setWxJsApiPrepay(WxPrepayInfo wxJsApiPrepay) {
        this.wxJsApiPrepay = wxJsApiPrepay;
    }

    public WxPrepayInfo getWxJsApiPrepay() {
        return wxJsApiPrepay;
    }

    public String getWxNativeCodeUrl() {
        return wxNativeCodeUrl;
    }

    public void setWxNativeCodeUrl(String wxNativeCodeUrl) {
        this.wxNativeCodeUrl = wxNativeCodeUrl;
    }

    public void setAlipay(AlipayPrepayInfo alipay) {
        this.alipay = alipay;
    }

    public AlipayPrepayInfo getAlipay() {
        return alipay;
    }
}
