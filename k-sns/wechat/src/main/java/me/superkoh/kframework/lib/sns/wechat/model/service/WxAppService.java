package me.superkoh.kframework.lib.sns.wechat.model.service;

import me.superkoh.kframework.lib.sns.wechat.common.exception.WxSnsException;
import me.superkoh.kframework.lib.sns.wechat.model.domain.WxAppSession;
import me.superkoh.kframework.lib.sns.wechat.model.domain.WxUser;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
public interface WxAppService {

    /**
     * 根据code获取小程序session信息
     *
     * @param code code
     * @return session
     */
    WxAppSession getSessionByCode(@NotBlank String code) throws WxSnsException;

    /**
     * 解码并保存用户信息
     *
     * @param encryptedUserInfo 加密信息
     * @param iv                iv
     * @param sessionKey        当前会话
     * @return 用户信息
     */
    WxUser decryptUserInfo(@NotBlank String encryptedUserInfo, @NotBlank String iv, @NotBlank String sessionKey);
}
