package me.superkoh.kframework.lib.payment.wechat.sdk.protocol;

import me.superkoh.kframework.lib.payment.common.service.info.WxPrepayInfo;
import me.superkoh.kframework.lib.payment.wechat.sdk.common.*;
import com.thoughtworks.xstream.XStream;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 支付请求, 响应数据
 */
public class WXPayData {
    private HashMap<String, Object> backupMap = new HashMap<>();

    public WXPayData() {}

    public void setValue(String key, Object value) {
        if (value == null || value == "") {
            backupMap.remove(key);
        } else {
            backupMap.put(key, value);
        }
    }

    public Object getValue(String key) {
        return backupMap.get(key);
    }

    public boolean isSet(String key) {
        Object obj = backupMap.get(key);
        return (null != obj);
    }

    public Map<String, Object> getAllValues() {
        Map<String,Object> map = new HashMap<>();
        Set<String> mapKeys = backupMap.keySet();

        for (String key : mapKeys) {
            Object obj = backupMap.get(key);
            if (obj != null) {
                map.put(key, obj);
            }
        }
        return map;
    }

    public void fromXml(String xml) throws Exception {
        XStream xStream = new XStream();
        xStream.registerConverter(new MapEntryConverter());
        xStream.alias("xml", Map.class);
        backupMap = (HashMap<String, Object>)xStream.fromXML(xml);
    }

    public String toXml() throws Exception {
        String xml = "<xml>";
        for (Map.Entry<String, Object> entry : backupMap.entrySet()) {
            //字段值不能为null，会影响后续流程
            if (entry.getValue() == null) {
                continue;
            }

            if (entry.getValue() instanceof Integer) {
                xml += "<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">";
            } else if (entry.getValue() instanceof String) {
                xml += "<" + entry.getKey() + ">"+ "<![CDATA[" + entry.getValue() + "]]></" + entry.getKey() + ">";
            } else {
                throw new Exception("WxPayData字段数据类型错误!");
            }
        }
        xml += "</xml>";
        return xml;
    }

    public static WXPayData downloadBillReqData(WXPerAppConfig appConfig, String billDate, String billType) {
        WXPayData downloadBillReqData = new WXPayData();
        downloadBillReqData.setValue(WXPayConstants.appIdKey, appConfig.getAppID());
        downloadBillReqData.setValue(WXPayConstants.mchIdKey, appConfig.getMchID());
        downloadBillReqData.setValue(WXPayConstants.deviceInfoKey, "WEB");

        downloadBillReqData.setValue(WXPayConstants.billDateKey, billDate);
        downloadBillReqData.setValue(WXPayConstants.billTypeKey, billType);

        addSignInfo(downloadBillReqData, appConfig);
        return downloadBillReqData;
}

    public static WXPayData payQueryReqData(WXPerAppConfig appConfig, String transactionID, String outTradeNo) {
        WXPayData payQueryReqData = new WXPayData();
        payQueryReqData.setValue(WXPayConstants.appIdKey, appConfig.getAppID());
        payQueryReqData.setValue(WXPayConstants.mchIdKey, appConfig.getMchID());

        payQueryReqData.setValue(WXPayConstants.transactionIdKey, transactionID);
        payQueryReqData.setValue(WXPayConstants.outTradeNoKey, outTradeNo);

        addSignInfo(payQueryReqData, appConfig);
        return payQueryReqData;
    }

    public static WXPayData closeOrderReqData(WXPerAppConfig appConfig, String outTradeNo) {
        WXPayData payQueryReqData = new WXPayData();
        payQueryReqData.setValue(WXPayConstants.appIdKey, appConfig.getAppID());
        payQueryReqData.setValue(WXPayConstants.mchIdKey, appConfig.getMchID());
        payQueryReqData.setValue(WXPayConstants.outTradeNoKey, outTradeNo);
        addSignInfo(payQueryReqData, appConfig);
        return payQueryReqData;
    }

    public static WXPayData refundReqData(WXPerAppConfig appConfig, String outTradeNo, int totalFee, int refundFee) {
        WXPayData refundReqData = new WXPayData();
        refundReqData.setValue(WXPayConstants.appIdKey, appConfig.getAppID());
        refundReqData.setValue(WXPayConstants.mchIdKey, appConfig.getMchID());
        refundReqData.setValue(WXPayConstants.deviceInfoKey, "WEB");

        refundReqData.setValue(WXPayConstants.outTradeNoKey, outTradeNo);
        refundReqData.setValue(WXPayConstants.outRefundNoKey, outTradeNo);
        refundReqData.setValue(WXPayConstants.totalFeeKey, totalFee);
        refundReqData.setValue(WXPayConstants.refundFeeKey, refundFee);
        refundReqData.setValue(WXPayConstants.opUserIdKey, appConfig.getMchID());
        refundReqData.setValue(WXPayConstants.refundFeeTypeKey, "CNY");

        addSignInfo(refundReqData, appConfig);
        return refundReqData;
    }

    public static WXPayData refundQueryReqData(WXPerAppConfig appConfig, String outTradeNo) {
        WXPayData refundQueryReqData = new WXPayData();
        refundQueryReqData.setValue(WXPayConstants.appIdKey, appConfig.getAppID());
        refundQueryReqData.setValue(WXPayConstants.mchIdKey, appConfig.getMchID());
        refundQueryReqData.setValue(WXPayConstants.deviceInfoKey, "WEB");

        refundQueryReqData.setValue(WXPayConstants.outTradeNoKey, outTradeNo);

        addSignInfo(refundQueryReqData, appConfig);
        return refundQueryReqData;
    }

    public static WXPayData revokeReqData(WXPerAppConfig appConfig, String transactionID, String outTradeNo) {
        WXPayData revokeReqData = new WXPayData();
        revokeReqData.setValue(WXPayConstants.appIdKey, appConfig.getAppID());
        revokeReqData.setValue(WXPayConstants.mchIdKey, appConfig.getMchID());

        revokeReqData.setValue(WXPayConstants.transactionIdKey, transactionID);
        revokeReqData.setValue(WXPayConstants.outTradeNoKey, outTradeNo);

        addSignInfo(revokeReqData, appConfig);
        return revokeReqData;
    }

    public static WXPayData scanPayReqData(WXPerAppConfig appConfig, String authCode, String body, String attach, String outTradeNo,
                                           int totalFee, String spBillCreateIP, String timeStart, String timeExpire,
                                           String goodsTag){
        WXPayData scanPayReqData = new WXPayData();
        scanPayReqData.setValue(WXPayConstants.appIdKey, appConfig.getAppID());
        scanPayReqData.setValue(WXPayConstants.mchIdKey, appConfig.getMchID());
        scanPayReqData.setValue(WXPayConstants.deviceInfoKey, "WEB");

        scanPayReqData.setValue(WXPayConstants.authCodeKey, authCode);
        scanPayReqData.setValue(WXPayConstants.bodyKey, body);
        scanPayReqData.setValue(WXPayConstants.attachKey, attach);
        scanPayReqData.setValue(WXPayConstants.outTradeNoKey, outTradeNo);
        scanPayReqData.setValue(WXPayConstants.totalFeeKey, totalFee);
        scanPayReqData.setValue(WXPayConstants.spbillCreateIpKey, spBillCreateIP);
        scanPayReqData.setValue(WXPayConstants.timeStartKey, timeStart);
        scanPayReqData.setValue(WXPayConstants.timeExpireKey, timeExpire);
        scanPayReqData.setValue(WXPayConstants.goodsTagKey, goodsTag);

        addSignInfo(scanPayReqData, appConfig);
        return scanPayReqData;
    }

    public static WXPayData unifiedOrderReqData(WXPerAppConfig appConfig, String body, String detail, String attach, String outTradeNo, String feeType, int totalFee, String spBillCreateIP, String timeStart, String timeExpire, String goodsTag, String tradeType, String productId, String openId) {
        WXPayData orderReqData = new WXPayData();
        orderReqData.setValue(WXPayConstants.appIdKey, appConfig.getAppID());
        orderReqData.setValue(WXPayConstants.mchIdKey, appConfig.getMchID());
        orderReqData.setValue(WXPayConstants.notifyUrlKey, appConfig.getNotifyUrl());
        orderReqData.setValue(WXPayConstants.deviceInfoKey, "WEB");

        orderReqData.setValue(WXPayConstants.bodyKey, body);
        orderReqData.setValue(WXPayConstants.detailKey, detail);
        orderReqData.setValue(WXPayConstants.attachKey, attach);
        orderReqData.setValue(WXPayConstants.outTradeNoKey, outTradeNo);
        orderReqData.setValue(WXPayConstants.feeTypeKey, feeType);
        orderReqData.setValue(WXPayConstants.totalFeeKey, totalFee);
        orderReqData.setValue(WXPayConstants.spbillCreateIpKey, spBillCreateIP);
        orderReqData.setValue(WXPayConstants.timeStartKey, timeStart);
        orderReqData.setValue(WXPayConstants.timeExpireKey, timeExpire);
        orderReqData.setValue(WXPayConstants.goodsTagKey, goodsTag);
        orderReqData.setValue(WXPayConstants.tradeTypeKey, tradeType);
        orderReqData.setValue(WXPayConstants.productIdKey, productId);
        orderReqData.setValue(WXPayConstants.openIdKey, openId);

        orderReqData.setValue(WXPayConstants.attachKey, appConfig.getAttachInfo());

        addSignInfo(orderReqData, appConfig);
        return orderReqData;
    }

    public static WXPayData prepayData(WXPerAppConfig appConfig, String prepayId) {
        WXPayData prepayData = new WXPayData();
        prepayData.setValue(WXPayConstants.prepayAppIdKey, appConfig.getAppID());
        prepayData.setValue(WXPayConstants.prepayNonceStrKey, RandomStringGenerator.getRandomStringByLength(32));
        prepayData.setValue(WXPayConstants.prepaySignTypeKey, "MD5");
        prepayData.setValue(WXPayConstants.prepayPackageKey, "prepay_id=" + prepayId);
        prepayData.setValue(WXPayConstants.prepayTimestampKey, Long.toString(System.currentTimeMillis() / 1000));

        String sign = Signature.getSign(prepayData.getAllValues(), appConfig.getKey());
        prepayData.setValue(WXPayConstants.prepaySignKey, sign);
        return prepayData;
    }

    public static WXPayData appPrepayData(WXPerAppConfig appConfig, WXPayData payResData) {
        WXPayData prepayData = new WXPayData();
        prepayData.setValue(WXPayConstants.appAppIdKey, appConfig.getAppID());
        prepayData.setValue(WXPayConstants.appPartnerIdKey, appConfig.getMchID());
        prepayData.setValue(WXPayConstants.appPrepayIdKey, payResData.getValue(WXPayConstants.prepayIdKey));
        prepayData.setValue(WXPayConstants.appPackageKey, "Sign=WXPay");
        prepayData.setValue(WXPayConstants.appNonceStrKey, payResData.getValue(WXPayConstants.nonceStrKey));
        prepayData.setValue(WXPayConstants.appTimestampKey, String.valueOf(Instant.now().getEpochSecond()));

        String sign = Signature.getSign(prepayData.getAllValues(), appConfig.getKey());
        prepayData.setValue(WXPayConstants.appSignKey, sign);
        return prepayData;
    }

    private static void addSignInfo(WXPayData payData, WXPerAppConfig appConfig) {
        payData.setValue(WXPayConstants.nonceStrKey, RandomStringGenerator.getRandomStringByLength(32));
        String sign = Signature.getSign(payData.getAllValues(), appConfig.getKey());
        payData.setValue(WXPayConstants.signKey, sign);
    }
}
