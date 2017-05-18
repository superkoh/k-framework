package me.superkoh.kframework.core.security.subject;

import me.superkoh.kframework.core.lang.BaseObject;

import java.time.LocalDateTime;

public abstract class LoginUserAdapter extends BaseObject implements LoginUser {
    private Long id;
    private String username;
    private String token;
    private LocalDateTime tokenExpireTime;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public LocalDateTime getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(LocalDateTime tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }
}
