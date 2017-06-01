package me.superkoh.kframework.lib.sns.wechat.open.api;

import me.superkoh.kframework.lib.sns.wechat.common.api.AbstractWxApi;
import me.superkoh.kframework.lib.sns.wechat.common.exception.WxSnsException;
import me.superkoh.kframework.lib.sns.wechat.open.res.WxOpenAccessTokenRes;
import me.superkoh.kframework.lib.sns.wechat.open.res.WxOpenUserInfoRes;
import okhttp3.Request;

/**
 * Created by KOH on 2017/6/1.
 * <p>
 * k-framework
 */
public class WxOpenApi extends AbstractWxApi {
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    private static final String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    public WxOpenApi(String appId, String secret) {
        super(appId, secret);
    }

    public WxOpenAccessTokenRes getAccessTokenByCode(String code) throws WxSnsException {
        Request request = new Request.Builder()
                .url(String.format(ACCESS_TOKEN_URL, getAppId(), getSecret(), code))
                .get()
                .build();
        return getResponse(request, WxOpenAccessTokenRes.class);
    }

    public WxOpenUserInfoRes getUserInfo(String accessToken, String openId) throws WxSnsException {
        Request request = new Request.Builder()
                .url(String.format(USER_INFO_URL, accessToken, openId))
                .get()
                .build();
        return getResponse(request, WxOpenUserInfoRes.class);
    }
}
