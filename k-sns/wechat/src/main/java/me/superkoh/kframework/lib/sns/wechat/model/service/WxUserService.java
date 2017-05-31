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

    /**
     * 根据小程序openId获取用户信息
     *
     * @param openId openId
     * @return 用户信息
     */
    WxUser getUserByAppOpenId(@NotEmpty String openId);

    /**
     * 根据开放平台openId获取用户信息
     *
     * @param openId openId
     * @return 用户信息
     */
    WxUser getUserByOpenOpenId(@NotEmpty String openId);

    /**
     * 根据公众号openId获取用户信息
     *
     * @param openId openId
     * @return 用户信息
     */
    WxUser getUserByMpOpenId(@NotEmpty String openId);
}
