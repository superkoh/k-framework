package me.superkoh.kframework.mvc.controller.annotation;

import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

/**
 * Created by KOH on 2017/5/17.
 * <p>
 * kmvc
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
public @interface KController {
}
