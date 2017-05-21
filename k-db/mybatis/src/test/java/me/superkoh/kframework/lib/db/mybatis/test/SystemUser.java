package me.superkoh.kframework.lib.db.mybatis.test;

import me.superkoh.kframework.core.security.subject.LoginUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created by KOH on 2017/5/21.
 * <p>
 * k-framework
 */
@Component("kLoginUser")
public class SystemUser implements LoginUser {
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
        return LocalDateTime.MAX;
    }
}
