package me.superkoh.kframework.mvc.security;

import java.time.LocalDateTime;

/**
 * Created by KOH on 16/8/4.
 */
public interface LoginUser {
    Long getId();

    @Deprecated
    String getUsername();

    String getToken();

    LocalDateTime getTokenExpireTime();
}
