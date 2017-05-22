package me.superkoh.kframework.lib.db.mybatis.annotation;

import java.lang.annotation.*;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Ignore {
}
