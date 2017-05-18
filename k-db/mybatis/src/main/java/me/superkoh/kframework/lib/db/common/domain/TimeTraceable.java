package me.superkoh.kframework.lib.db.common.domain;

import java.time.LocalDateTime;

public interface TimeTraceable {
    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);
}
