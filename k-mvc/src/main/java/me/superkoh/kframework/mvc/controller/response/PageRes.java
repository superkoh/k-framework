package me.superkoh.kframework.mvc.controller.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import me.superkoh.kframework.core.type.PageList;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApiModel
public class PageRes<T extends BizRes> extends ListRes<T> {

    @ApiModelProperty(required = true, value = "页码")
    private Integer pageSize;
    @ApiModelProperty(required = true, value = "每页数量")
    private Integer pageNo;
    @ApiModelProperty(value = "全部数量")
    private Long totalCnt;

    public <I> PageRes(PageList<I> pageList, Function<? super I, ? extends T> mapper) {
        this(pageList.getList().stream().map(mapper).collect(Collectors.toList()), pageList.getPageSize(), pageList
                .getPageNo(), pageList.getTotalCnt());
    }

    public PageRes(List<T> list, Integer pageSize, Integer pageNo, Long totalCnt) {
        super(list);
        this.pageSize = pageSize;
        this.pageNo = pageNo;
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

    public Long getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(Long totalCnt) {
        this.totalCnt = totalCnt;
    }
}

