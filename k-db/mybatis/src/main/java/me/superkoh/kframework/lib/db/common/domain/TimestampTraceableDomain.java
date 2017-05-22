package me.superkoh.kframework.lib.db.common.domain;

public abstract class TimestampTraceableDomain extends BaseDomain implements TimestampTraceable {
    private Long createTime;
    private Long updateTime;

    @Override
    public Long getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public Long getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
