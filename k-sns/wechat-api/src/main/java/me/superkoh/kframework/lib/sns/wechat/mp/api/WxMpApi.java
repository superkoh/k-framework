package me.superkoh.kframework.lib.sns.wechat.mp.api;

import me.superkoh.kframework.lib.sns.wechat.common.api.AbstractWxApi;
import me.superkoh.kframework.lib.sns.wechat.common.exception.WxSnsException;
import me.superkoh.kframework.lib.sns.wechat.mp.res.WxMpAuthAccessTokenRes;
import me.superkoh.kframework.lib.sns.wechat.mp.res.WxMpUserInfoRes;
import okhttp3.Request;

/**
 * Created by KOH on 2017/6/1.
 * <p>
 * k-framework
 */
public class WxMpApi extends AbstractWxApi {
    private static final String AUTH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    private static final String AUTH_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    public WxMpApi(String appId, String secret) {
        super(appId, secret);
    }

    public WxMpAuthAccessTokenRes getAccessTokenByCode(String code) throws WxSnsException {
        Request request = new Request.Builder()
                .url(String.format(AUTH_ACCESS_TOKEN_URL, getAppId(), getSecret(), code))
                .get()
                .build();
        return getResponse(request, WxMpAuthAccessTokenRes.class);
    }

    public WxMpUserInfoRes getAuthUserInfo(String accessToken, String openId) throws WxSnsException {
        Request request = new Request.Builder()
                .url(String.format(AUTH_USER_INFO_URL, accessToken, openId))
                .get()
                .build();
        return getResponse(request, WxMpUserInfoRes.class);
    }
}
