package me.superkoh.kframework.lib.db.common.domain;

import java.time.LocalDateTime;

public abstract class TimeTraceableDomain extends BaseDomain implements TimeTraceable {
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Override
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
