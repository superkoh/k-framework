package me.superkoh.kframework.lib.payment.common.service.info;

/**
 * Created by zhangyh on 16/9/22.
 */
public class AlipayPrepayInfo {
    /**
     * 网页端支付表格数据
     */
    private String formHtml;
    /**
     * App端支付订单数据
     */
    private String appOrderString;

    public AlipayPrepayInfo(String formHtml, String orderString) {
        this.formHtml = formHtml;
        this.appOrderString = orderString;
    }

    public void setFormHtml(String formHtml) {
        this.formHtml = formHtml;
    }

    public String getFormHtml() {
        return formHtml;
    }

    public String getAppOrderString() {
        return appOrderString;
    }

    public void setAppOrderString(String appOrderString) {
        this.appOrderString = appOrderString;
    }
}
