package me.superkoh.kframework.mvc.config.profiles;

import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Profile(ProfileConstant.RT_WEB)
@Configuration
@Import({EmbeddedServletContainerAutoConfiguration.class, WebMvcAutoConfiguration.class})
public class BaseWebProfile {
}
