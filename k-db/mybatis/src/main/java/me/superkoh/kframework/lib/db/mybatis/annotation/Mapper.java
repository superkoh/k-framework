package me.superkoh.kframework.lib.db.mybatis.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by KOH on 2017/5/18.
 * <p>
 * k-lib
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Mapper {
}
