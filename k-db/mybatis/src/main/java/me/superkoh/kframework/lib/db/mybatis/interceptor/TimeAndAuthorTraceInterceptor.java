package me.superkoh.kframework.lib.db.mybatis.interceptor;

import me.superkoh.kframework.core.runtime.RuntimeEnv;
import me.superkoh.kframework.lib.db.common.domain.AuthorTraceable;
import me.superkoh.kframework.lib.db.common.domain.TimeTraceable;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class TimeAndAuthorTraceInterceptor implements Interceptor {
    @Autowired
    private RuntimeEnv env;

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
        if (record instanceof AuthorTraceable) {
            AuthorTraceable authorTraceablePO = (AuthorTraceable) record;
            if (mappedStatement.getSqlCommandType().equals(SqlCommandType.INSERT)) {
                authorTraceablePO.setCreateUser(env.getLoginUser().getId());
            }
            authorTraceablePO.setUpdateUser(env.getLoginUser().getId());
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
