package me.superkoh.kframework.mvc.controller.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SuccessRes extends BizRes {
    @ApiModelProperty(required = true, value = "返回码")
    private Integer ok = 0;
    @ApiModelProperty(value = "返回数据")
    private BizRes obj;
    private String vd;

    public SuccessRes() {
    }

    public SuccessRes(BizRes obj) {
        this.obj = obj;
    }

    public Integer getOk() {
        return ok;
    }

    public void setOk(Integer ok) {
        this.ok = ok;
    }

    public BizRes getObj() {
        return obj;
    }

    public void setObj(BizRes obj) {
        this.obj = obj;
    }

    public String getVd() {
        return vd;
    }

    public void setVd(String vd) {
        this.vd = vd;
    }
}
