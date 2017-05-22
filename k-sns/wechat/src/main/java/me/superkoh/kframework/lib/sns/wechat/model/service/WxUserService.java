package me.superkoh.kframework.lib.sns.wechat.model.service;

import me.superkoh.kframework.lib.sns.wechat.model.domain.WxUser;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
public interface WxUserService {

    /**
     * 根据ID获取用户信息
     *
     * @param unionId unionId
     * @return 用户信息
     */
    WxUser getUserByUnionId(@NotEmpty String unionId);
}
