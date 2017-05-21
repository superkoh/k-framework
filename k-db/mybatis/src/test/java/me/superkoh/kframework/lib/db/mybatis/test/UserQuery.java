package me.superkoh.kframework.lib.db.mybatis.test;

import me.superkoh.kframework.lib.db.mybatis.annotation.Column;

/**
 * Created by KOH on 2017/5/21.
 * <p>
 * k-framework
 */
public class UserQuery {
    private Long id;
    @Column(column = "id")
    private Long minId;
    @Column(column = "id")
    private Long maxId;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMinId() {
        return minId;
    }

    public void setMinId(Long minId) {
        this.minId = minId;
    }

    public Long getMaxId() {
        return maxId;
    }

    public void setMaxId(Long maxId) {
        this.maxId = maxId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
