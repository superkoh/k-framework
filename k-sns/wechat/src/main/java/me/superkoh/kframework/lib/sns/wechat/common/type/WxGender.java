package me.superkoh.kframework.lib.sns.wechat.common.type;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
public enum WxGender {
    MALE,
    FEMALE,
    UNKNOWN;

    public static WxGender of(Integer gender) {
        switch (gender) {
            case 1:
                return MALE;
            case 2:
                return FEMALE;
            default:
                return UNKNOWN;
        }
    }
}
