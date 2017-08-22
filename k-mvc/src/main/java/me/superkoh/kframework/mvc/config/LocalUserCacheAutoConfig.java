package me.superkoh.kframework.mvc.config;

import me.superkoh.kframework.mvc.config.profiles.ProfileConstant;
import me.superkoh.kframework.mvc.security.LoginUser;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;

@ConfigurationProperties("kframework.mvc.local-user-cache")
@Profile(ProfileConstant.RT_WEB)
public class LocalUserCacheAutoConfig {
    private Long expire = 60L;
    private Long capacity = 10000L;

    @Bean("localUserCache")
    Cache<String, LoginUser> localUserCache() {
        return new Cache2kBuilder<String, LoginUser>() {
        }
                .name("localUserCache")
                .expireAfterWrite(expire, TimeUnit.SECONDS)
                .entryCapacity(capacity)
                .build();
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }
}
