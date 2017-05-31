package me.superkoh.kframework.lib.payment.wechat.sdk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.superkoh.kframework.lib.payment.wechat.sdk.protocol.auth_access_token_protocol.AuthAccessTokenResData;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zhangyh on 16/9/14.
 */
public class GetOpenIdService {
    private static RestTemplate restTemplate = new RestTemplate();
    private static final String weixinOAuthUrl = "https://api.weixin.qq.com/sns/oauth2/";

    public static String getOpenIdByCode(String appId, String appSecret, String code) {
        AuthAccessTokenResData accessTokenRes;
        String responseStr = restTemplate.getForObject(authAccessTokenUrl(appId, appSecret, code), String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            accessTokenRes = mapper.readValue(responseStr, AuthAccessTokenResData.class);
        } catch (Exception e) {
            accessTokenRes = new AuthAccessTokenResData();
        }

        if (accessTokenRes.getErrcode() == 0 && null != accessTokenRes.getOpenid() &&
                !accessTokenRes.getOpenid().isEmpty()) {
            return accessTokenRes.getOpenid();
        }
        return null;
    }

    private static String authAccessTokenUrl(String appId, String appSecret, String code) {
        return weixinOAuthUrl + "access_token?&grant_type=authorization_code&appid=" + appId + "&secret=" +
                appSecret + "&code=" + code;
    }
}
