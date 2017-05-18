package me.superkoh.kframework.lib.db.mybatis.annotation;

import java.lang.annotation.*;

/**
 * Created by KOH on 2017/5/13.
 * <p>
 * pte-server
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PK {
}
