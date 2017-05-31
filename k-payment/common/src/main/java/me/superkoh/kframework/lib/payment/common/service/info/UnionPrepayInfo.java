package me.superkoh.kframework.lib.payment.common.service.info;

import java.util.Map;

/**
 * Created by zhangyh on 16/9/8.
 */
public class UnionPrepayInfo {
    private String payUrl;
    private Map<String, String> params;
    private String formHtml;

    public UnionPrepayInfo(String payUrl, Map<String, String> params, String formHtml) {
        this.payUrl = payUrl;
        this.params = params;
        this.formHtml = formHtml;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setFormHtml(String formHtml) {
        this.formHtml = formHtml;
    }

    public String getFormHtml() {
        return formHtml;
    }
}
