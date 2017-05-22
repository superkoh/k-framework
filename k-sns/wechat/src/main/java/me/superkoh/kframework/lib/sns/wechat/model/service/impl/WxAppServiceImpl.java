package me.superkoh.kframework.lib.sns.wechat.model.service.impl;

import me.superkoh.kframework.lib.sns.wechat.app.api.WxAppApi;
import me.superkoh.kframework.lib.sns.wechat.app.res.WxAppSessionRes;
import me.superkoh.kframework.lib.sns.wechat.app.res.WxAppUserInfoRes;
import me.superkoh.kframework.lib.sns.wechat.model.domain.WxAppSession;
import me.superkoh.kframework.lib.sns.wechat.model.domain.WxUser;
import me.superkoh.kframework.lib.sns.wechat.common.exception.WxSnsException;
import me.superkoh.kframework.lib.sns.wechat.model.mapper.WxAppSessionMapper;
import me.superkoh.kframework.lib.sns.wechat.model.mapper.WxUserMapper;
import me.superkoh.kframework.lib.sns.wechat.model.service.WxAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
@Service
@Validated
public class WxAppServiceImpl implements WxAppService {
    @Autowired
    private WxAppApi wxAppApi;
    @Autowired
    private WxAppSessionMapper wxAppSessionMapper;
    @Autowired
    private WxUserMapper wxUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxAppSession getSessionByCode(String code) throws WxSnsException {
        WxAppSessionRes session = wxAppApi.jscode2session(code);
        WxAppSession wxAppSession = new WxAppSession(session);
        wxAppSessionMapper.insert(wxAppSession);
        return wxAppSession;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxUser decryptUserInfo(String encryptedUserInfo, String iv, String sessionKey) {
        WxAppUserInfoRes userInfo = wxAppApi.decryptUserInfo(encryptedUserInfo, iv, sessionKey);
        WxUser wxUser = wxUserMapper.selectByAppOpenId(userInfo.getOpenId());
        if (null == wxUser) {
            wxUser = new WxUser(userInfo);
            wxUserMapper.insert(wxUser);
            return wxUser;
        } else {
            WxUser toUpdate = new WxUser(userInfo);
            toUpdate.setId(wxUser.getId());
            wxUserMapper.updateByPrimaryKeySelective(toUpdate);
            return wxUserMapper.selectByAppOpenId(userInfo.getOpenId());
        }
    }
}
