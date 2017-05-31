package me.superkoh.kframework.lib.payment.common.service.info;

/**
 * Created by zhangyh on 16/9/22.
 */
public class AlipayPrepayInfo {
    private String formHtml;

    public AlipayPrepayInfo(String formHtml) {
        this.formHtml = formHtml;
    }

    public void setFormHtml(String formHtml) {
        this.formHtml = formHtml;
    }

    public String getFormHtml() {
        return formHtml;
    }
}
