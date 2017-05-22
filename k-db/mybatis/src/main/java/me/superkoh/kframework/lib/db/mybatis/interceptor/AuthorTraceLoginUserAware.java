package me.superkoh.kframework.lib.db.mybatis.interceptor;

import me.superkoh.kframework.core.security.subject.LoginUser;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
public interface AuthorTraceLoginUserAware {
    LoginUser getLoginUser();
}
