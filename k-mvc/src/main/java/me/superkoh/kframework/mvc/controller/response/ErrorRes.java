package me.superkoh.kframework.mvc.controller.response;

import me.superkoh.kframework.core.exception.KException;
import me.superkoh.kframework.core.exception.KRuntimeException;

public class ErrorRes extends BizRes {
    private Integer ok = -1;
    private String msg = "未知错误";
    private String vd;

    public ErrorRes() {
    }

    public ErrorRes(Integer ok, String msg) {
        this.ok = ok;
        this.msg = msg;
    }

    public ErrorRes(KException e) {
        this.ok = e.getCode();
        this.msg = e.getMessage();
    }

    public ErrorRes(KRuntimeException e) {
        this.ok = e.getCode();
        this.msg = e.getMessage();
    }

    public Integer getOk() {
        return ok;
    }

    public void setOk(Integer ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVd() {
        return vd;
    }

    public void setVd(String vd) {
        this.vd = vd;
    }
}
