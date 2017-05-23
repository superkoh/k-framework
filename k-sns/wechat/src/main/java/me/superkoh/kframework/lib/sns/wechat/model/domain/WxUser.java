package me.superkoh.kframework.lib.sns.wechat.model.domain;

import me.superkoh.kframework.lib.db.mybatis.annotation.PK;
import me.superkoh.kframework.lib.sns.wechat.app.res.WxAppUserInfoRes;
import me.superkoh.kframework.lib.sns.wechat.common.type.WxGender;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
public class WxUser {
    @PK
    private Long id;
    private String openIdForMp;
    private String openIdForOpen;
    private String openIdForApp;
    private String unionId;
    private String nickname;
    private WxGender gender;
    private String country;
    private String province;
    private String city;
    private String avatarUrl;

    public WxUser() {
    }

    public WxUser(WxAppUserInfoRes userInfo) {
        this.openIdForApp = userInfo.getOpenId();
        this.unionId = userInfo.getUnionId();
        this.nickname = userInfo.getNickName();
        this.gender = WxGender.of(userInfo.getGender());
        this.country = userInfo.getCountry();
        this.province = userInfo.getProvince();
        this.city = userInfo.getCity();
        this.avatarUrl = userInfo.getAvatarUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenIdForMp() {
        return openIdForMp;
    }

    public void setOpenIdForMp(String openIdForMp) {
        this.openIdForMp = openIdForMp;
    }

    public String getOpenIdForOpen() {
        return openIdForOpen;
    }

    public void setOpenIdForOpen(String openIdForOpen) {
        this.openIdForOpen = openIdForOpen;
    }

    public String getOpenIdForApp() {
        return openIdForApp;
    }

    public void setOpenIdForApp(String openIdForApp) {
        this.openIdForApp = openIdForApp;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public WxGender getGender() {
        return gender;
    }

    public void setGender(WxGender gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

