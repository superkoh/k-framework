package me.superkoh.kframework.mvc.controller.request;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by jill on 17/1/11.
 */
public abstract class TransferableReq<T> {
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
}
