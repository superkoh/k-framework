package me.superkoh.kframework.core.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Created by KOH on 2017/4/24.
 * <p>
 * webFramework
 */
@Configuration
public class MethodValidationPostProcessorAutoConfig {
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
