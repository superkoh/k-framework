package me.superkoh.kframework.lib.db.mybatis.interceptor;

import me.superkoh.kframework.core.security.subject.LoginUser;
import me.superkoh.kframework.lib.db.common.domain.AuthorTraceable;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.util.Properties;

/**
 * Created by KOH on 2017/5/18.
 * <p>
 * k-framework
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AuthorTraceInterceptor implements Interceptor {

    private AuthorTraceLoginUserAware loginUserAware;

    public AuthorTraceInterceptor(AuthorTraceLoginUserAware loginUserAware) {
        this.loginUserAware = loginUserAware;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        LoginUser loginUser = loginUserAware.getLoginUser();
        if (null == loginUser) {
            return invocation.proceed();
        }
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object record = invocation.getArgs()[1];
        if (record instanceof AuthorTraceable) {
            AuthorTraceable authorTraceableRecord = (AuthorTraceable) record;
            if (mappedStatement.getSqlCommandType().equals(SqlCommandType.INSERT)) {
                authorTraceableRecord.setCreateUser(loginUser.getId());
            }
            authorTraceableRecord.setUpdateUser(loginUser.getId());
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
