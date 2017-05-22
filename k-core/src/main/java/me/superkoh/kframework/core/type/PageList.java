package me.superkoh.kframework.core.type;

import me.superkoh.kframework.core.lang.BaseObject;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by KOH on 16/7/15.
 */
public class PageList<T> extends BaseObject {
    private List<T> list;
    private Long totalCnt;
    private Integer pageSize;
    private Integer pageNo;

    public PageList(List<T> list, Page page) {
        this.list = list;
        this.pageSize = page.getPageSize();
        this.pageNo = page.getPageNo();
    }

    public PageList(List<T> list, Page page, Long totalCnt) {
        this.list = list;
        this.pageSize = page.getPageSize();
        this.pageNo = page.getPageNo();
        this.totalCnt = totalCnt;
    }

    public <I> PageList(PageList<I> pageList, Function<? super I, ? extends T> mapper) {
        this.list = pageList.list.stream().map(mapper).collect(Collectors.toList());
        this.pageSize = pageList.pageSize;
        this.pageNo = pageList.pageNo;
        this.totalCnt = pageList.totalCnt;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(Long totalCnt) {
        this.totalCnt = totalCnt;
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
}
