package me.superkoh.kframework.core.security.subject;

import me.superkoh.kframework.core.lang.BaseObject;

import java.time.LocalDateTime;

/**
 * Created by KOH on 2016/12/19.
 */
public class SystemUser extends BaseObject implements LoginUser {
    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public String getUsername() {
        return "system";
    }

    @Override
    public String getToken() {
        return "system";
    }

    @Override
    public LocalDateTime getTokenExpireTime() {
        return LocalDateTime.now();
    }

    @Override
    public boolean getAutoTrace() {
        return true;
    }


}
