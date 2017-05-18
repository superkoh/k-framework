package me.superkoh.kframework.core.security.subject;

import me.superkoh.kframework.core.lang.BaseObject;

import java.time.LocalDateTime;

/**
 * Created by KOH on 2016/12/29.
 */
public class GuestUser extends BaseObject implements LoginUser {
    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public String getUsername() {
        return "guest";
    }

    @Override
    public String getToken() {
        return "guest";
    }

    @Override
    public LocalDateTime getTokenExpireTime() {
        return LocalDateTime.now();
    }
}
