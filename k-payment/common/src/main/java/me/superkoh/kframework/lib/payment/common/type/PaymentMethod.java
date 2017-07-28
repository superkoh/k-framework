package me.superkoh.kframework.lib.payment.common.type;

/**
 * 支付方式
 * Created by zhangyh on 2016/10/10.
 */
public enum PaymentMethod {
    UNION_PC(PaymentChannel.WEB),
    UNION_WAP(PaymentChannel.WAP),
    WEIXIN_JSAPI(PaymentChannel.WEIXIN),
    WEIXIN_NATIVE(PaymentChannel.WEB),
    WEIXIN_APP(PaymentChannel.APP),
    ALIPAY_DIRECT(PaymentChannel.WEB),
    ALIPAY_WAP(PaymentChannel.WAP),
    ALIPAY_APP(PaymentChannel.APP),
    OFFLINE_TRANSFER(PaymentChannel.OFFLINE);

    private PaymentChannel channel;
    PaymentMethod(PaymentChannel channel) {
        this.channel = channel;
    }

    public PaymentChannel getChannel() {
        return this.channel;
    }
}
