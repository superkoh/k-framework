package me.superkoh.kframework.lib.db.mybatis.interceptor;

import me.superkoh.kframework.lib.db.common.domain.TimeTraceable;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.time.LocalDateTime;
import java.util.Properties;

/**
 * Created by KOH on 2017/5/18.
 * <p>
 * k-framework
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class TimeTraceInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object record = invocation.getArgs()[1];
        if (record instanceof TimeTraceable) {
            TimeTraceable timeTraceableRecord = (TimeTraceable) record;
            LocalDateTime now = LocalDateTime.now();
            if (mappedStatement.getSqlCommandType().equals(SqlCommandType.INSERT)) {
                timeTraceableRecord.setCreateTime(now);
            }
            timeTraceableRecord.setUpdateTime(now);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
