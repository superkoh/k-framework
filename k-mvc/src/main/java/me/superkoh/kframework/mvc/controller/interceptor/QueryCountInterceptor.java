package me.superkoh.kframework.mvc.controller.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class})})
public class QueryCountInterceptor implements Interceptor, LogFilterInterceptor {
    private ThreadLocal<Integer> queryCount = new ThreadLocal<>();

    public Object intercept(Invocation invocation) throws Throwable {
        Integer count = queryCount.get();
        if (count != null) {
            queryCount.set(count + 1);
        }
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }

    @Override
    public void prepare() {
        queryCount.set(0);
    }

    @Override
    public String getOutput() {
        String output = queryCount.get().toString();
        queryCount.remove();
        return output;
    }

    @Override
    public String getKey() {
        return "dbCount";
    }
}
