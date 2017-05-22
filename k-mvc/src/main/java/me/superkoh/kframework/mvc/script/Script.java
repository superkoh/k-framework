package me.superkoh.kframework.mvc.script;

import me.superkoh.kframework.mvc.config.profiles.ProfileConstant;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(ProfileConstant.RT_SCRIPT)
@Component
public @interface Script {
    String value() default "";
}
