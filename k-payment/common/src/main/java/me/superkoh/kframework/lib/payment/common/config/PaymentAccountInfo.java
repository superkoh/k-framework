package me.superkoh.kframework.lib.payment.common.config;

/**
 * 支付账号配置信息
 * Created by zhangyh on 2017/5/23.
 */
public class PaymentAccountInfo {
    /**
     * 是否测试环境
     */
    private Boolean isDebugMode;


    /**
     * 微信支付AppId
     */
    private String wxAppId;
    /**
     * 微信支付App密钥
     */
    private String wxAppSecret;
    /**
     * 微信支付 支付密钥
     */
    private String wxPaySecret;
    /**
     * 微信支付商户号
     */
    private String wxMchId;
    /**
     * 微信支付证书本地存放路径
     */
    private String wxCertLocalPath;
    /**
     * 微信支付证书文件密码
     */
    private String wxCertPassword;


    /**
     * 支付宝签约合作商户ID
     */
    private String aliPartnerId;
    /**
     * 支付宝支付AppID
     */
    private String aliAppId;
    /**
     * 支付宝支付私钥
     */
    private String aliAppPrivateKey;
    /**
     * 支付宝公钥
     */
    private String aliAppPublicKey;
    /**
     * 支付宝支付链接
     */
    private String aliServerUrl;
    /**
     * 支付宝支付完成支付结果页面
     */
    private String aliReturnUrl;
    /**
     * 支付宝后台通知链接
     */
    private String aliNotifyUrl;


    /**
     * 银联支付商户号
     */
    private String unionMerId;
    /**
     * 银联支付前端通知页面
     */
    private String unionFrontUrl;
    /**
     * 银联支付后台通知链接
     */
    private String unionBackUrl;
    /**
     * 银联支付签名证书路径
     */
    private String unionSignCertPath;
    /**
     * 银联支付签名证书密码
     */
    private String unionSignCertPwd;
    /**
     * 银联支付签名证书类型， 固定为PKCS12
     */
    private String unionSignCertType;
    /**
     * 银联支付验证签名证书目录
     */
    private String unionValidateCertDir;
    /**
     * 银联支付敏感信息加密证书路径
     */
    private String unionEncryptCertPath;
    /**
     * 银联支付签名证书路径
     */
    private String unionSingleMode;


    public Boolean getIsDebugMode() {
        return isDebugMode;
    }

    public void setIsDebugMode(Boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getWxAppSecret() {
        return wxAppSecret;
    }

    public void setWxAppSecret(String wxAppSecret) {
        this.wxAppSecret = wxAppSecret;
    }

    public String getWxPaySecret() {
        return wxPaySecret;
    }

    public void setWxPaySecret(String wxPaySecret) {
        this.wxPaySecret = wxPaySecret;
    }

    public String getWxMchId() {
        return wxMchId;
    }

    public void setWxMchId(String wxMchId) {
        this.wxMchId = wxMchId;
    }

    public String getWxCertLocalPath() {
        return wxCertLocalPath;
    }

    public void setWxCertLocalPath(String wxCertLocalPath) {
        this.wxCertLocalPath = wxCertLocalPath;
    }

    public String getWxCertPassword() {
        return wxCertPassword;
    }

    public void setWxCertPassword(String wxCertPassword) {
        this.wxCertPassword = wxCertPassword;
    }

    public String getAliPartnerId() {
        return aliPartnerId;
    }

    public void setAliPartnerId(String aliPartnerId) {
        this.aliPartnerId = aliPartnerId;
    }

    public String getAliAppId() {
        return aliAppId;
    }

    public void setAliAppId(String aliAppId) {
        this.aliAppId = aliAppId;
    }

    public String getAliAppPrivateKey() {
        return aliAppPrivateKey;
    }

    public void setAliAppPrivateKey(String aliAppPrivateKey) {
        this.aliAppPrivateKey = aliAppPrivateKey;
    }

    public String getAliAppPublicKey() {
        return aliAppPublicKey;
    }

    public void setAliAppPublicKey(String aliAppPublicKey) {
        this.aliAppPublicKey = aliAppPublicKey;
    }

    public String getAliServerUrl() {
        return aliServerUrl;
    }

    public void setAliServerUrl(String aliServerUrl) {
        this.aliServerUrl = aliServerUrl;
    }

    public String getAliReturnUrl() {
        return aliReturnUrl;
    }

    public void setAliReturnUrl(String aliReturnUrl) {
        this.aliReturnUrl = aliReturnUrl;
    }

    public String getAliNotifyUrl() {
        return aliNotifyUrl;
    }

    public void setAliNotifyUrl(String aliNotifyUrl) {
        this.aliNotifyUrl = aliNotifyUrl;
    }

    public String getUnionMerId() {
        return unionMerId;
    }

    public void setUnionMerId(String unionMerId) {
        this.unionMerId = unionMerId;
    }

    public String getUnionFrontUrl() {
        return unionFrontUrl;
    }

    public void setUnionFrontUrl(String unionFrontUrl) {
        this.unionFrontUrl = unionFrontUrl;
    }

    public String getUnionBackUrl() {
        return unionBackUrl;
    }

    public void setUnionBackUrl(String unionBackUrl) {
        this.unionBackUrl = unionBackUrl;
    }

    public String getUnionSignCertPath() {
        return unionSignCertPath;
    }

    public void setUnionSignCertPath(String unionSignCertPath) {
        this.unionSignCertPath = unionSignCertPath;
    }

    public String getUnionSignCertPwd() {
        return unionSignCertPwd;
    }

    public void setUnionSignCertPwd(String unionSignCertPwd) {
        this.unionSignCertPwd = unionSignCertPwd;
    }

    public String getUnionSignCertType() {
        return unionSignCertType;
    }

    public void setUnionSignCertType(String unionSignCertType) {
        this.unionSignCertType = unionSignCertType;
    }

    public String getUnionValidateCertDir() {
        return unionValidateCertDir;
    }

    public void setUnionValidateCertDir(String unionValidateCertDir) {
        this.unionValidateCertDir = unionValidateCertDir;
    }

    public String getUnionEncryptCertPath() {
        return unionEncryptCertPath;
    }

    public void setUnionEncryptCertPath(String unionEncryptCertPath) {
        this.unionEncryptCertPath = unionEncryptCertPath;
    }

    public String getUnionSingleMode() {
        return unionSingleMode;
    }

    public void setUnionSingleMode(String unionSingleMode) {
        this.unionSingleMode = unionSingleMode;
    }
}
