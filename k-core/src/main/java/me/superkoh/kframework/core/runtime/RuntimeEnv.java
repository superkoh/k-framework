package me.superkoh.kframework.core.runtime;

import me.superkoh.kframework.core.security.subject.LoginUser;
import me.superkoh.kframework.core.security.subject.SystemUser;

public class RuntimeEnv {
    private LoginUser loginUser;

    public RuntimeEnv() {
        this.loginUser = new SystemUser();
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }
}
