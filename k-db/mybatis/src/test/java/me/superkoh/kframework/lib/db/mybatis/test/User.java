package me.superkoh.kframework.lib.db.mybatis.test;

import me.superkoh.kframework.lib.db.common.domain.TimeAndAuthorTraceableDomain;
import me.superkoh.kframework.lib.db.mybatis.annotation.PK;

/**
 * Created by KOH on 2017/5/19.
 * <p>
 * k-framework
 */
public class User extends TimeAndAuthorTraceableDomain {
    @PK
    private Long id;
    private String name;
    private UserSex gender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserSex getGender() {
        return gender;
    }

    public void setGender(UserSex gender) {
        this.gender = gender;
    }
}
