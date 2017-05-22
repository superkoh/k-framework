package me.superkoh.kframework.lib.db.common.domain;

public interface TimestampTraceable {
    Long getCreateTime();

    void setCreateTime(Long createTime);

    Long getUpdateTime();

    void setUpdateTime(Long updateTime);
}
