package me.superkoh.kframework.mvc.controller.annotation;

import me.superkoh.kframework.mvc.config.profiles.ProfileConstant;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * Created by KOH on 2017/5/17.
 * <p>
 * kmvc
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@Profile(ProfileConstant.RT_WEB)
public @interface KRestController {
}
