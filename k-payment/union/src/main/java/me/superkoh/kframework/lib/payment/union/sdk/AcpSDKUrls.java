package me.superkoh.kframework.lib.payment.union.sdk;

/**
 * Created by zhangyh on 2017/5/23.
 */
public class AcpSDKUrls {
    private static final String ACPSDK_PROD_FRONT_TRANS_URL = "https://gateway.95516.com/gateway/api/frontTransReq.do";
    private static final String ACPSDK_PROD_BACK_TRANS_URL = "https://gateway.95516.com/gateway/api/backTransReq.do";
    private static final String ACPSDK_PROD_SINGLE_QUERY_URL = "https://gateway.95516.com/gateway/api/queryTrans.do";
    private static final String ACPSDK_PROD_BATCH_TRANS_URL = "https://gateway.95516.com/gateway/api/batchTrans.do";
    private static final String ACPSDK_PROD_FILE_TRANS_URL = "https://filedownload.95516.com/";
    private static final String ACPSDK_PROD_APP_TRANS_URL = "https://gateway.95516.com/gateway/api/appTransReq.do";
    private static final String ACPSDK_PROD_CARD_TRANS_URL = "https://gateway.95516.com/gateway/api/cardTransReq.do";

    private static final String ACPSDK_PROD_JF_FRONT_TRANS_URL = "https://gateway.95516.com/jiaofei/api/frontTransReq.do";
    private static final String ACPSDK_PROD_JF_BACK_TRANS_URL = "https://gateway.95516.com/jiaofei/api/backTransReq.do";
    private static final String ACPSDK_PROD_JF_SINGLE_QUERY_URL = "https://gateway.95516.com/jiaofei/api/queryTrans.do";
    private static final String ACPSDK_PROD_JF_CARD_TRANS_URL = "https://gateway.95516.com/jiaofei/api/cardTransReq.do";
    private static final String ACPSDK_PROD_JF_APP_TRANS_URL = "https://gateway.95516.com/jiaofei/api/appTransReq.do";

    private static final String ACPSDK_TEST_FRONT_TRANS_URL = "https://101.231.204.80:5000/gateway/api/frontTransReq.do";
    private static final String ACPSDK_TEST_BACK_TRANS_URL = "https://101.231.204.80:5000/gateway/api/backTransReq.do";
    private static final String ACPSDK_TEST_SINGLE_QUERY_URL = "https://101.231.204.80:5000/gateway/api/queryTrans.do";
    private static final String ACPSDK_TEST_BATCH_TRANS_URL = "https://101.231.204.80:5000/gateway/api/batchTrans.do";
    private static final String ACPSDK_TEST_FILE_TRANS_URL = "https://101.231.204.80:9080/";
    private static final String ACPSDK_TEST_APP_TRANS_URL = "https://101.231.204.80:5000/gateway/api/appTransReq.do";
    private static final String ACPSDK_TEST_CARD_TRANS_URL = "https://101.231.204.80:5000/gateway/api/cardTransReq.do";

    private static final String ACPSDK_TEST_JF_FRONT_TRANS_URL = "https://101.231.204.80:5000/jiaofei/api/frontTransReq.do";
    private static final String ACPSDK_TEST_JF_BACK_TRANS_URL = "https://101.231.204.80:5000/jiaofei/api/backTransReq.do";
    private static final String ACPSDK_TEST_JF_SINGLE_QUERY_URL = "https://101.231.204.80:5000/jiaofei/api/queryTrans.do";
    private static final String ACPSDK_TEST_JF_CARD_TRANS_URL = "https://101.231.204.80:5000/jiaofei/api/cardTransReq.do";
    private static final String ACPSDK_TEST_JF_APP_TRANS_URL = "https://101.231.204.80:5000/jiaofei/api/appTransReq.do";

    /** 前台请求URL. */
    private String frontRequestUrl;
    /** 后台请求URL. */
    private String backRequestUrl;
    /** 单笔查询 */
    private String singleQueryUrl;
    /** 批量查询 */
    private String batchQueryUrl;
    /** 批量交易 */
    private String batchTransUrl;
    /** 文件传输 */
    private String fileTransUrl;

    /** 有卡交易. */
    private String cardRequestUrl;
    /** app交易 */
    private String appRequestUrl;

    /*缴费相关地址*/
    private String jfFrontRequestUrl;
    private String jfBackRequestUrl;
    private String jfSingleQueryUrl;
    private String jfCardRequestUrl;
    private String jfAppRequestUrl;

    private static AcpSDKUrls prodUrls;
    private static AcpSDKUrls testUrls;

    public static AcpSDKUrls getProdUrls() {
        if (null == prodUrls) {
            prodUrls = new AcpSDKUrls();
            prodUrls.frontRequestUrl   = ACPSDK_PROD_FRONT_TRANS_URL;
            prodUrls.backRequestUrl    = ACPSDK_PROD_BACK_TRANS_URL;
            prodUrls.singleQueryUrl    = ACPSDK_PROD_SINGLE_QUERY_URL;
            prodUrls.batchTransUrl     = ACPSDK_PROD_BATCH_TRANS_URL;
            prodUrls.fileTransUrl      = ACPSDK_PROD_FILE_TRANS_URL;
            prodUrls.cardRequestUrl    = ACPSDK_PROD_CARD_TRANS_URL;
            prodUrls.appRequestUrl     = ACPSDK_PROD_APP_TRANS_URL;
            prodUrls.jfFrontRequestUrl = ACPSDK_PROD_JF_FRONT_TRANS_URL;
            prodUrls.jfBackRequestUrl  = ACPSDK_PROD_JF_BACK_TRANS_URL;
            prodUrls.jfSingleQueryUrl  = ACPSDK_PROD_JF_SINGLE_QUERY_URL;
            prodUrls.jfCardRequestUrl  = ACPSDK_PROD_JF_CARD_TRANS_URL;
            prodUrls.jfAppRequestUrl   = ACPSDK_PROD_JF_APP_TRANS_URL;
        }
        return prodUrls;
    }

    public static AcpSDKUrls getTestUrls() {
        if (null == testUrls) {
            testUrls = new AcpSDKUrls();
            testUrls.frontRequestUrl   = ACPSDK_TEST_FRONT_TRANS_URL;
            testUrls.backRequestUrl    = ACPSDK_TEST_BACK_TRANS_URL;
            testUrls.singleQueryUrl    = ACPSDK_TEST_SINGLE_QUERY_URL;
            testUrls.batchTransUrl     = ACPSDK_TEST_BATCH_TRANS_URL;
            testUrls.fileTransUrl      = ACPSDK_TEST_FILE_TRANS_URL;
            testUrls.cardRequestUrl    = ACPSDK_TEST_CARD_TRANS_URL;
            testUrls.appRequestUrl     = ACPSDK_TEST_APP_TRANS_URL;
            testUrls.jfFrontRequestUrl = ACPSDK_TEST_JF_FRONT_TRANS_URL;
            testUrls.jfBackRequestUrl  = ACPSDK_TEST_JF_BACK_TRANS_URL;
            testUrls.jfSingleQueryUrl  = ACPSDK_TEST_JF_SINGLE_QUERY_URL;
            testUrls.jfCardRequestUrl  = ACPSDK_TEST_JF_CARD_TRANS_URL;
            testUrls.jfAppRequestUrl   = ACPSDK_TEST_JF_APP_TRANS_URL;
        }
        return testUrls;
    }

    public String getFrontRequestUrl() {
        return frontRequestUrl;
    }

    public void setFrontRequestUrl(String frontRequestUrl) {
        this.frontRequestUrl = frontRequestUrl;
    }

    public String getBackRequestUrl() {
        return backRequestUrl;
    }

    public void setBackRequestUrl(String backRequestUrl) {
        this.backRequestUrl = backRequestUrl;
    }

    public String getSingleQueryUrl() {
        return singleQueryUrl;
    }

    public void setSingleQueryUrl(String singleQueryUrl) {
        this.singleQueryUrl = singleQueryUrl;
    }

    public String getBatchQueryUrl() {
        return batchQueryUrl;
    }

    public void setBatchQueryUrl(String batchQueryUrl) {
        this.batchQueryUrl = batchQueryUrl;
    }

    public String getBatchTransUrl() {
        return batchTransUrl;
    }

    public void setBatchTransUrl(String batchTransUrl) {
        this.batchTransUrl = batchTransUrl;
    }

    public String getFileTransUrl() {
        return fileTransUrl;
    }

    public void setFileTransUrl(String fileTransUrl) {
        this.fileTransUrl = fileTransUrl;
    }

    public String getCardRequestUrl() {
        return cardRequestUrl;
    }

    public void setCardRequestUrl(String cardRequestUrl) {
        this.cardRequestUrl = cardRequestUrl;
    }

    public String getAppRequestUrl() {
        return appRequestUrl;
    }

    public void setAppRequestUrl(String appRequestUrl) {
        this.appRequestUrl = appRequestUrl;
    }

    public String getJfFrontRequestUrl() {
        return jfFrontRequestUrl;
    }

    public void setJfFrontRequestUrl(String jfFrontRequestUrl) {
        this.jfFrontRequestUrl = jfFrontRequestUrl;
    }

    public String getJfBackRequestUrl() {
        return jfBackRequestUrl;
    }

    public void setJfBackRequestUrl(String jfBackRequestUrl) {
        this.jfBackRequestUrl = jfBackRequestUrl;
    }

    public String getJfSingleQueryUrl() {
        return jfSingleQueryUrl;
    }

    public void setJfSingleQueryUrl(String jfSingleQueryUrl) {
        this.jfSingleQueryUrl = jfSingleQueryUrl;
    }

    public String getJfCardRequestUrl() {
        return jfCardRequestUrl;
    }

    public void setJfCardRequestUrl(String jfCardRequestUrl) {
        this.jfCardRequestUrl = jfCardRequestUrl;
    }

    public String getJfAppRequestUrl() {
        return jfAppRequestUrl;
    }

    public void setJfAppRequestUrl(String jfAppRequestUrl) {
        this.jfAppRequestUrl = jfAppRequestUrl;
    }
}
