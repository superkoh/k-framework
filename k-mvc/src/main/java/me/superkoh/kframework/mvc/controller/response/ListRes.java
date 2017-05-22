package me.superkoh.kframework.mvc.controller.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class ListRes<T extends BizRes> extends BizRes {
    @ApiModelProperty(required = true, value = "列表")
    private List<T> list;

    public ListRes(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
