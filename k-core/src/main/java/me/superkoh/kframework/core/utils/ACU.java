package me.superkoh.kframework.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by KOH on 16/6/30.
 */
@Component
public class ACU implements ApplicationContextAware {
    private static ApplicationContext ctx;

    public static ApplicationContext ctx() {
        return ctx;
    }

    public static Object bean(String name) {
        return ctx().getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
}
