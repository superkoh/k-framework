package me.superkoh.kframework.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by KOH on 16/6/30.
 */
@Component
public class ACU implements ApplicationContextAware {
    private static ApplicationContext ctx;
    private static Environment env;

    public static ApplicationContext ctx() {
        return ctx;
    }

    public static Environment env() {
        if (null == env) {
            env = (Environment) bean("environment");
        }
        return env;
    }

    public static Object bean(String name) {
        try {
            return ctx().getBean(name);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T bean(String name, Class<T> clazz) {
        try {
            return ctx().getBean(name, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return env().getProperty(key, targetType, defaultValue);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
}
