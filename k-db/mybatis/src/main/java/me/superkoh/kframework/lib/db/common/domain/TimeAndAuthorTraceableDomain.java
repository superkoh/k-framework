package me.superkoh.kframework.lib.db.common.domain;

public abstract class TimeAndAuthorTraceableDomain extends TimeTraceableDomain implements AuthorTraceable {
    private Long createUser;
    private Long updateUser;

    @Override
    public Long getCreateUser() {
        return createUser;
    }

    @Override
    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    @Override
    public Long getUpdateUser() {
        return updateUser;
    }

    @Override
    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }
}
