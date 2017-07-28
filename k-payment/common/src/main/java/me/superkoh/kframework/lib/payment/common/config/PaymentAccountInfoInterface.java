package me.superkoh.kframework.lib.payment.common.config;

/**
 * Created by zhangyh on 2017/7/28.
 * 支付账号接口
 */
public interface PaymentAccountInfoInterface {
    String UnionPayEncodingParamKey = "encoding";

    Boolean getIsDebugMode();

    String getWxAppId();

    String getWxAppSecret();

    String getWxPaySecret();

    String getWxMchId();

    String getWxCertLocalPath();

    String getWxCertPassword();

    String getAliPartnerId();

    String getAliAppId();

    String getAliAppPrivateKey();

    String getAliAppPublicKey();

    String getAliServerUrl();

    String getAliReturnUrl();

    String getAliNotifyUrl();

    String getUnionMerId();

    String getUnionFrontUrl();

    String getUnionBackUrl();

    String getUnionSignCertPath();

    String getUnionSignCertPwd();

    String getUnionSignCertType();

    String getUnionValidateCertDir();

    String getUnionEncryptCertPath();

    String getUnionSingleMode();
}
