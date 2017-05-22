package me.superkoh.kframework.mvc.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import me.superkoh.kframework.core.type.Page;

import javax.validation.constraints.NotNull;

@ApiModel
public class PageReq {
    @NotNull
    @ApiModelProperty(required = true, value = "每页数量")
    private Integer pageSize = 20;
    @NotNull
    @ApiModelProperty(required = true, value = "页码")
    private Integer pageNo = 1;
    @ApiModelProperty(required = true, value = "排序")
    private String orderBy;

    public Page toPage() {
        return new Page(pageSize, pageNo, orderBy);
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
