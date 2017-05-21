package me.superkoh.kframework.lib.db.mybatis.interceptor;

import me.superkoh.kframework.core.security.subject.LoginUser;
import me.superkoh.kframework.core.utils.ACU;
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
public class AuthorTraceInterceptor<T extends LoginUser> implements Interceptor {

    private Class<T> userClazz;

    public AuthorTraceInterceptor(Class<T> userClazz) {
        this.userClazz = userClazz;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        LoginUser loginUser = ACU.currentUser();
        if (null == loginUser || !loginUser.getClass().equals(userClazz)) {
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
