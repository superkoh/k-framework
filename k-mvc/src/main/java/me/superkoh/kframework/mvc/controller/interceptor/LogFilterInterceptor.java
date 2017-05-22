package me.superkoh.kframework.mvc.controller.interceptor;

/**
 * Created by KOH on 2017/2/9.
 * <p>
 * webFramework
 */
public interface LogFilterInterceptor {

    void prepare();

    String getOutput();

    String getKey();
}
