package me.superkoh.kframework.lib.sns.wechat.app.bean;

/**
 * Created by KOH on 2017/4/25.
 * <p>
 * webFramework
 */
public class WxAppErrorRes {
    private Integer errcode;
    private String errmsg;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
