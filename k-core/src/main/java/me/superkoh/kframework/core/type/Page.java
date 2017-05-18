package me.superkoh.kframework.core.type;

import me.superkoh.kframework.core.lang.BaseObject;

public class Page extends BaseObject {
    private Integer pageSize;
    private Integer pageNo;
    private String orderBy;

    public Page(Integer pageSize, Integer pageNo) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    public Page(Integer pageSize, Integer pageNo, String orderBy) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.orderBy = orderBy;
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
