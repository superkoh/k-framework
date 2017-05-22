package me.superkoh.kframework.mvc.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import me.superkoh.kframework.core.type.Page;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by jill on 17/1/11.
 */
@ApiModel
public abstract class TransferablePageReq<T> {
    @NotNull
    @ApiModelProperty(required = true, value = "每页数量")
    private Integer pageSize = 20;
    @NotNull
    @ApiModelProperty(required = true, value = "页码")
    private Integer pageNo = 1;
    @ApiModelProperty(required = true, value = "排序")
    private String orderBy;

    public T toBean() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class<T> clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        try {
            T bean = clazz.newInstance();
            BeanUtils.copyProperties(this, bean);
            return bean;
        } catch (InstantiationException | IllegalAccessException ignored) {
        }
        return null;
    }

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
