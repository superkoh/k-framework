package me.superkoh.kframework.lib.payment.wechat.service;

import me.superkoh.kframework.core.exception.KException;
import me.superkoh.kframework.core.utils.DateTimeHelper;
import me.superkoh.kframework.lib.payment.common.config.PaymentAccountInfoInterface;
import me.superkoh.kframework.lib.payment.common.service.ThirdPartyPayService;
import me.superkoh.kframework.lib.payment.common.service.info.*;
import me.superkoh.kframework.lib.payment.common.type.PaymentChannel;
import me.superkoh.kframework.lib.payment.common.type.PaymentServiceStatus;
import me.superkoh.kframework.lib.payment.common.type.PaymentStatus;
import me.superkoh.kframework.lib.payment.wechat.sdk.WXPay;
import me.superkoh.kframework.lib.payment.wechat.sdk.common.Configure;
import me.superkoh.kframework.lib.payment.wechat.sdk.common.Signature;
import me.superkoh.kframework.lib.payment.wechat.sdk.common.WXPayConstants;
import me.superkoh.kframework.lib.payment.wechat.sdk.common.WXPerAppConfig;
import me.superkoh.kframework.lib.payment.wechat.sdk.protocol.WXPayData;
import me.superkoh.kframework.lib.payment.wechat.sdk.service.GetOpenIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.time.Instant;
import java.util.Map;

/**
 * 微信支付服务
 * Created by zhangyh on 16/9/14.
 */
@Service(value = "WxPay")
public class WxPayServiceImpl implements ThirdPartyPayService {

    private static final Logger logger = LoggerFactory.getLogger("paymentLogger");

    @PostConstruct
    public void init() {
        String myIpAddress = "127.0.0.1";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            myIpAddress = addr.getHostAddress();
        } catch (Exception ignored) {
        }

        WXPay.initSDKConfiguration(myIpAddress);
    }

    @Override
    public PaymentPrepayInfo getPrepayInfo(ThirdPartyRequestPayInfo requestPayInfo, PaymentAccountInfoInterface accountInfo) throws Exception {
        String outTradeNo = accountInfo.getTradePrefix() + requestPayInfo.getTradeId();
        String startTime = DateTimeHelper.stringOfCurrentTimeAtChina("yyyyMMddHHmmss");
        String expiredTime = DateTimeHelper.stringOfTimestampAtChina(maxExpireTime(requestPayInfo), "yyyyMMddHHmmss");
        String productId = requestPayInfo.getProductId();

        PaymentPrepayInfo prepayInfo = new PaymentPrepayInfo();
        if (requestPayInfo.getChannel().equals(PaymentChannel.WEIXIN)) {
            WXPerAppConfig appConfig = extractWxConfig(accountInfo);
            WxPrepayInfo wxPrepayInfo = getUnifiedOrderInfoForJsapiPay(appConfig, outTradeNo, requestPayInfo.getAmount(), startTime, expiredTime, productId, requestPayInfo.getProductName(), requestPayInfo.getAuthCode(), requestPayInfo.getOpenId(), requestPayInfo.getUserIp());
            prepayInfo.setWxJsApiPrepay(wxPrepayInfo);
        } else {
            WXPerAppConfig appConfig = extractWxConfig(accountInfo);
            String payUrl = getUnifiedOrderInfoForNativePay(appConfig, outTradeNo, requestPayInfo.getAmount(), startTime, expiredTime,
                    productId, requestPayInfo.getProductName());
            prepayInfo.setWxNativeCodeUrl(payUrl);
        }

        return prepayInfo;
    }

    @Override
    public PaymentStatusInfo queryPayResult(String tradeId, Long tradeTime, PaymentAccountInfoInterface accountInfo) throws Exception {
        tradeId = accountInfo.getTradePrefix() + tradeId;
        PaymentStatusInfo statusInfo = new PaymentStatusInfo();
        statusInfo.setTradeIdWithPrefix(tradeId, accountInfo.getTradePrefix());
        WXPerAppConfig appConfig = extractWxConfig(accountInfo);

        try {
            WXPayData queryResult = queryOrderStatus(tradeId, appConfig);
            statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);

            if (queryResult.getValue(WXPayConstants.resultCodeKey).equals("FAIL")) {
                String errCode = (String)queryResult.getValue(WXPayConstants.errCodeKey);
                String errMsg = (String)queryResult.getValue(WXPayConstants.errCodeDesKey);
                if (errCode.equals("ORDERNOTEXIST")) {
                    statusInfo.setStatus(PaymentStatus.CLOSED);
                    logger.info("[关闭订单]Case5: 成功 订单不存在(当作关闭处理)");
                } else {
                    statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
                    logger.error("[查询订单]Case4: 失败，错误码：" + errCode + " 错误信息：" + errMsg);
                }
            } else {
                switch ((String)queryResult.getValue(WXPayConstants.tradeStateKey)) {
                    case "SUCCESS":
                        statusInfo.setStatus(PaymentStatus.SUCCESS);
                        statusInfo.setTradeIdWithPrefix((String) queryResult.getValue(WXPayConstants.outTradeNoKey), accountInfo.getTradePrefix());
                        statusInfo.setTotalAmount((String) queryResult.getValue(WXPayConstants.totalFeeKey));
                        if (null != queryResult.getValue(WXPayConstants.timeEndKey)) {
                            String timeEndStr = (String) queryResult.getValue(WXPayConstants.timeEndKey);
                            statusInfo.setPaymentTime(DateTimeHelper.timestampOfDateTimeStringAtChina(timeEndStr, "yyyyMMddHHmmss"));
                        }
                        break;
                    case "REFUND":
                        statusInfo.setStatus(PaymentStatus.CLOSED);
                        break;
                    case "NOTPAY":
                        statusInfo.setStatus(PaymentStatus.NOT_PAY);
                        break;
                    case "CLOSED":
                        statusInfo.setStatus(PaymentStatus.CLOSED);
                        break;
                    case "REVOKED":
                        statusInfo.setStatus(PaymentStatus.CLOSED);
                        break;
                    case "USERPAYING":
                        statusInfo.setStatus(PaymentStatus.NOT_PAY);
                        break;
                    case "PAYERROR":
                        statusInfo.setStatus(PaymentStatus.PAY_ERROR);
                        break;
                    default:
                        statusInfo.setStatus(PaymentStatus.NOT_PAY);
                        break;
                }
                logger.info("[查询订单]Case5: 成功 交易状态：" + queryResult.getValue(WXPayConstants.tradeStateKey));
            }
        } catch (Exception e) {
            statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
            statusInfo.setServiceErrorDesc(e.getMessage());
        }

        return statusInfo;
    }

    @Override
    public PaymentStatusInfo closeUnfinishedPay(String tradeId, PaymentAccountInfoInterface accountInfo) throws Exception {
        tradeId = accountInfo.getTradePrefix() + tradeId;
        PaymentStatusInfo statusInfo = new PaymentStatusInfo();
        statusInfo.setTradeIdWithPrefix(tradeId, accountInfo.getTradePrefix());
        WXPerAppConfig appConfig = extractWxConfig(accountInfo);

        try {
            WXPayData closeResult = closeUnfinishedOrder(tradeId, appConfig);
            statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);
            // 处理业务相关错误
            if (closeResult.getValue(WXPayConstants.resultCodeKey).equals("FAIL")) {
                String errCode = (String)closeResult.getValue(WXPayConstants.errCodeKey);
                String errMsg = (String)closeResult.getValue(WXPayConstants.errCodeDesKey);
                switch (errCode) {
                    case "ORDERPAID":
                        statusInfo.setStatus(PaymentStatus.SUCCESS);
                        statusInfo.setTradeIdWithPrefix((String) closeResult.getValue(WXPayConstants.outTradeNoKey), accountInfo.getTradePrefix());
                        statusInfo.setTotalAmount((String) closeResult.getValue(WXPayConstants.totalFeeKey));
                        if (null != closeResult.getValue(WXPayConstants.timeEndKey)) {
                            String timeEndStr = (String) closeResult.getValue(WXPayConstants.timeEndKey);
                            statusInfo.setPaymentTime(DateTimeHelper.timestampOfDateTimeStringAtChina(timeEndStr, "yyyyMMddHHmmss"));
                        }
                        logger.info("[关闭订单]Case4: 失败 订单已支付");
                        break;
                    case "ORDERNOTEXIST":
                        statusInfo.setStatus(PaymentStatus.CLOSED);
                        logger.info("[关闭订单]Case5: 成功 订单不存在(当作关闭处理)");
                        break;
                    case "ORDERCLOSED":
                        statusInfo.setStatus(PaymentStatus.CLOSED);
                        logger.info("[关闭订单]Case5: 成功 订单已关闭");
                        break;
                    default:
                        statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
                        statusInfo.setServiceErrorCode(errCode);
                        statusInfo.setServiceErrorDesc(errMsg);
                        logger.error("[关闭订单]Case4: 失败，错误码：" + errCode + " 错误信息：" + errMsg);
                        break;
                }
            } else {
                statusInfo.setStatus(PaymentStatus.CLOSED);
                logger.info("[关闭订单]Case5: 成功");
            }
        } catch (Exception e) {
            statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
            statusInfo.setServiceErrorDesc(e.getMessage());
        }

        return statusInfo;
    }

    @Override
    public PaymentStatusInfo queryRefundState(String tradeId, PaymentAccountInfoInterface accountInfo) throws Exception {
        tradeId = accountInfo.getTradePrefix() + tradeId;
        PaymentStatusInfo statusInfo = new PaymentStatusInfo();
        statusInfo.setTradeIdWithPrefix(tradeId, accountInfo.getTradePrefix());
        WXPerAppConfig appConfig = extractWxConfig(accountInfo);

        try {
            WXPayData refundState = queryRefundState_internal(tradeId, appConfig);
            statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);

            // 处理业务相关错误
            if (refundState.getValue(WXPayConstants.resultCodeKey).equals("FAIL")) {
                String errCode = (String)refundState.getValue(WXPayConstants.errCodeKey);
                String errMsg = (String)refundState.getValue(WXPayConstants.errCodeDesKey);

                statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
                statusInfo.setServiceErrorCode(errCode);
                statusInfo.setServiceErrorDesc(errMsg);
                logger.error("[查询退款]失败，错误码：" + errCode + " 错误信息：" + errMsg);
            } else {
                String refundStatus = (String)refundState.getValue(WXPayConstants.refundStatusPrefixKey + 0);
                if ("SUCCESS".equals(refundStatus)) {
                    String refundTime = (String)refundState.getValue(WXPayConstants.refundSuccessTimePrefixKey + 0);
                    statusInfo.setStatus(PaymentStatus.REFUNDED);
                    statusInfo.setRefundTime(DateTimeHelper.timestampOfDateTimeStringAtChina(refundTime, "yyyy-MM-dd HH:mm:ss"));
                } else if ("PROCESSING".equals(refundStatus)) {
                    statusInfo.setStatus(PaymentStatus.APPLIED_REFUND);
                } else {
                    statusInfo.setStatus(PaymentStatus.REFUND_FAILED);
                }
                logger.info("[查询退款]成功, 退款状态: " + refundStatus);
            }
        } catch (Exception e) {
            statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
            statusInfo.setServiceErrorDesc(e.getMessage());
            logger.error("[查询退款]失败，" + " 错误信息：" + e.getLocalizedMessage());
        }

        return statusInfo;
    }

    @Override
    public PaymentStatusInfo applyRefund(String tradeId, int totalFee, PaymentAccountInfoInterface accountInfo) throws Exception {
        tradeId = accountInfo.getTradePrefix() + tradeId;
        PaymentStatusInfo statusInfo = new PaymentStatusInfo();
        statusInfo.setTradeIdWithPrefix(tradeId, accountInfo.getTradePrefix());
        WXPerAppConfig appConfig = extractWxConfig(accountInfo);

        try {
            WXPayData refundState = applyRefund_internal(tradeId, totalFee, appConfig);
            statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);

            // 处理业务相关错误
            if (refundState.getValue(WXPayConstants.resultCodeKey).equals("FAIL")) {
                String errCode = (String)refundState.getValue(WXPayConstants.errCodeKey);
                String errMsg = (String)refundState.getValue(WXPayConstants.errCodeDesKey);

                statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
                statusInfo.setServiceErrorCode(errCode);
                statusInfo.setServiceErrorDesc(errMsg);
                logger.error("[申请退款]失败，错误码：" + errCode + " 错误信息：" + errMsg);
            } else {
                statusInfo.setStatus(PaymentStatus.APPLIED_REFUND);
                logger.info("[申请退款]成功");
            }
        } catch (Exception e) {
            statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
            statusInfo.setServiceErrorDesc(e.getMessage());
            logger.error("[申请退款]失败，" + " 错误信息：" + e.getLocalizedMessage());
        }

        return statusInfo;
    }

    @Override
    public PaymentNotifyProcessInfo handleBackNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfoInterface accountInfo) throws Exception {
        PaymentNotifyProcessInfo notifyStatus = new PaymentNotifyProcessInfo();
        notifyStatus.setResponseEncoding("UTF-8");
        notifyStatus.setResponseContentType("text/html;charset=UTF-8");
        WXPerAppConfig wechatInfo = extractWxConfig(accountInfo);

        String notifyStr = notifyParams.get("notifyStr");
        logger.info("收到微信支付回调" + notifyStr);
        WXPayData res = WXPay.verifyResultNotify(notifyStr, wechatInfo.getKey());
        if (res.isSet(WXPayConstants.returnCodeKey)) {
            notifyStatus.getStatusInfo().setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
            notifyStatus.setResponseBody(res.toXml());
        } else {
            WXPayData notifyData = new WXPayData();
            notifyData.fromXml(notifyStr);
            if (!notifyData.isSet(WXPayConstants.transactionIdKey)) {
                res.setValue("return_code", "FAIL");
                res.setValue("return_msg", "支付结果中微信订单号不存在");
                logger.error("支付结果中微信订单号不存在");
                notifyStatus.getStatusInfo().setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
                notifyStatus.setResponseBody(res.toXml());
            } else {
                if (!wechatInfo.getAppID().equals(notifyData.getValue(WXPayConstants.appIdKey)) ||
                        !wechatInfo.getMchID().equals(notifyData.getValue(WXPayConstants.mchIdKey))) {
                    res.setValue("return_code", "FAIL");
                    res.setValue("return_msg", "支付结果中appId, mchId不匹配");
                    logger.info("微信支付结果通知失败，appId，mchId不匹配");
                } else {
                    res.setValue("return_code", "SUCCESS");
                    res.setValue("return_msg", "OK");

                    notifyStatus.getStatusInfo().setServiceStatus(PaymentServiceStatus.SUCCESS);
                    notifyStatus.setResponseBody(res.toXml());
                    notifyStatus.getStatusInfo().setStatus(PaymentStatus.SUCCESS);
                    notifyStatus.getStatusInfo().setTradeIdWithPrefix((String) notifyData.getValue(WXPayConstants.outTradeNoKey), accountInfo.getTradePrefix());
                    notifyStatus.getStatusInfo().setTotalAmount((String) notifyData.getValue(WXPayConstants.totalFeeKey));
                    if (null != notifyData.getValue(WXPayConstants.timeEndKey)) {
                        String timeEndStr = (String) notifyData.getValue(WXPayConstants.timeEndKey);
                        notifyStatus.getStatusInfo().setPaymentTime(DateTimeHelper.timestampOfDateTimeStringAtChina(timeEndStr, "yyyyMMddHHmmss"));
                    }
                    logger.info("微信支付结果通知成功");
                }
            }
        }

        return notifyStatus;
    }

    @Override
    public PaymentNotifyProcessInfo handleFrontNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfoInterface accountInfo) throws Exception {
        return null;
    }

    @Override
    public PaymentNotifyProcessInfo getFailedNotifyStatus(String message) throws Exception {
        WXPayData resData = new WXPayData();
        resData.setValue("return_code", "FAIL");
        resData.setValue("return_msg", message);

        PaymentNotifyProcessInfo notifyStatus = new PaymentNotifyProcessInfo();
        notifyStatus.getStatusInfo().setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
        notifyStatus.setResponseEncoding("UTF-8");
        notifyStatus.setResponseContentType("text/html;charset=UTF-8");
        notifyStatus.setResponseBody(resData.toXml());
        return notifyStatus;
    }

    private WxPrepayInfo getUnifiedOrderInfoForJsapiPay(WXPerAppConfig wechatInfo, String outTradeNo, int amount, String startTime, String expiredTime, String productId, String productName, String code, String openId, String userIp) throws Exception {
        if ((null == code || code.trim().isEmpty()) && (null == openId || openId.trim().isEmpty())) {
            throw new KException("微信支付授权码不能为空");
        }
        if (null == openId || openId.trim().isEmpty()) {
            openId = GetOpenIdService.getOpenIdByCode(wechatInfo.getAppID(), wechatInfo.getAppSecret(), code);
        }
        if (null == openId || openId.trim().isEmpty()) {
            throw new KException("无法获取微信openId");
        }

        WXPayData orderReqData = WXPayData.unifiedOrderReqData(wechatInfo, productName, "", "", outTradeNo, "CNY",
                amount, userIp, startTime, expiredTime, "", "JSAPI", productId, openId);
        WXPayData orderResData = processUnifiedOrderByReqData(orderReqData, wechatInfo);
        WXPayData prepayData = WXPayData.prepayData(wechatInfo, (String)orderResData.getValue(WXPayConstants.prepayIdKey));

        WxPrepayInfo prepayInfo = new WxPrepayInfo();
        prepayInfo.setNonceStr((String)prepayData.getValue(WXPayConstants.prepayNonceStrKey));
        prepayInfo.setPackageStr((String)prepayData.getValue(WXPayConstants.prepayPackageKey));
        prepayInfo.setPaySign((String)prepayData.getValue(WXPayConstants.prepaySignKey));
        prepayInfo.setSignType((String)prepayData.getValue(WXPayConstants.prepaySignTypeKey));
        prepayInfo.setTimestamp((String)prepayData.getValue(WXPayConstants.prepayTimestampKey));
        return prepayInfo;
    }

    private String getUnifiedOrderInfoForNativePay(WXPerAppConfig wechatInfo, String outTradeNo, int amount,
                                                   String startTime, String expiredTime,
                                                   String productId, String productName) throws Exception {
        WXPayData orderReqData = WXPayData.unifiedOrderReqData(wechatInfo, productName, null, null, outTradeNo, "CNY",
                amount, Configure.getIP(), startTime, expiredTime, null, "NATIVE", productId, null);
        WXPayData orderResData = processUnifiedOrderByReqData(orderReqData, wechatInfo);
        return (String)orderResData.getValue(WXPayConstants.codeUrlKey);
    }

    private WXPayData queryOrderStatus(String outTradeNo, WXPerAppConfig appConfig) throws  Exception {
        WXPayData orderReqData = WXPayData.payQueryReqData(appConfig, null, outTradeNo);
        return processQueryOrderByReqData(orderReqData, appConfig);
    }

    private WXPayData closeUnfinishedOrder(String outTradeNo, WXPerAppConfig appConfig) throws Exception {
        WXPayData orderReqData = WXPayData.closeOrderReqData(appConfig, outTradeNo);
        return processCloseOrderByReqData(orderReqData, appConfig);
    }

    private WXPayData queryRefundState_internal(String outTradeNo, WXPerAppConfig appConfig) throws Exception {
        WXPayData orderReqData = WXPayData.refundQueryReqData(appConfig, outTradeNo);
        return processQueryRefundByReqData(orderReqData, appConfig);
    }

    private WXPayData applyRefund_internal(String outTradeNo, int totalFee, WXPerAppConfig appConfig) throws Exception {
        WXPayData orderReqData = WXPayData.refundReqData(appConfig, outTradeNo, totalFee);
        return processApplyRefundByReqData(orderReqData, appConfig);
    }

    private WXPayData processUnifiedOrderByReqData(WXPayData orderReqData, WXPerAppConfig appConfig) throws Exception {
        String orderServiceResponseString;

        orderServiceResponseString = WXPay.requestUnifiedOrderService(orderReqData, appConfig);
        logger.info("统一下单API返回的数据如下：");
        logger.info(orderServiceResponseString);

        //将从API返回的XML数据映射到Java对象
        WXPayData orderResData = new WXPayData();
        orderResData.fromXml(orderServiceResponseString);
        validateApiResponseData("统一下单", orderServiceResponseString, orderResData, appConfig.getKey());

        // 处理业务相关错误
        if (orderResData.getValue(WXPayConstants.resultCodeKey).equals("FAIL")) {
            logger.error("[统一下单]Case4: 失败，错误码：" + orderResData.getValue(WXPayConstants.errCodeKey) +
                    "     错误信息：" + orderResData.getValue(WXPayConstants.errCodeDesKey));
            throw new KException("[统一下单]失败");
        } else {
            logger.info("[统一下单]Case5: 成功 交易类型: " + orderResData.getValue(WXPayConstants.tradeTypeKey) +
                    " prepayId: " + orderResData.getValue(WXPayConstants.prepayIdKey) +
                    " codeUrl: " + orderResData.getValue(WXPayConstants.codeUrlKey));
        }

        return orderResData;
    }

    private WXPayData processQueryOrderByReqData(WXPayData orderReqData, WXPerAppConfig appConfig) throws Exception {
        String orderServiceResponseString;

        orderServiceResponseString = WXPay.requestPayQueryService(orderReqData, appConfig);
        logger.info("查询订单API返回的数据如下：");
        logger.info(orderServiceResponseString);

        //将从API返回的XML数据映射到Java对象
        WXPayData orderResData = new WXPayData();
        orderResData.fromXml(orderServiceResponseString);
        validateApiResponseData("查询订单", orderServiceResponseString, orderResData, appConfig.getKey());
        return orderResData;
    }

    private WXPayData processCloseOrderByReqData(WXPayData orderReqData, WXPerAppConfig appConfig) throws Exception {
        String orderServiceResponseString;

        orderServiceResponseString = WXPay.requestCloseService(orderReqData, appConfig);
        logger.info("关闭订单API返回的数据如下：");
        logger.info(orderServiceResponseString);

        //将从API返回的XML数据映射到Java对象
        WXPayData orderResData = new WXPayData();
        orderResData.fromXml(orderServiceResponseString);
        validateApiResponseData("关闭订单", orderServiceResponseString, orderResData, appConfig.getKey());
        return orderResData;
    }

    private WXPayData processQueryRefundByReqData(WXPayData orderReqData, WXPerAppConfig appConfig) throws Exception {
        String orderServiceResponseString;

        orderServiceResponseString = WXPay.requestRefundQueryService(orderReqData, appConfig);
        logger.info("查询退款API返回的数据如下：");
        logger.info(orderServiceResponseString);

        //将从API返回的XML数据映射到Java对象
        WXPayData orderResData = new WXPayData();
        orderResData.fromXml(orderServiceResponseString);
        validateApiResponseData("查询退款", orderServiceResponseString, orderResData, appConfig.getKey());
        return orderResData;
    }

    private WXPayData processApplyRefundByReqData(WXPayData orderReqData, WXPerAppConfig appConfig) throws Exception {
        String orderServiceResponseString;

        orderServiceResponseString = WXPay.requestRefundService(orderReqData, appConfig);
        logger.info("申请退款API返回的数据如下：");
        logger.info(orderServiceResponseString);

        //将从API返回的XML数据映射到Java对象
        WXPayData orderResData = new WXPayData();
        orderResData.fromXml(orderServiceResponseString);
        validateApiResponseData("申请退款", orderServiceResponseString, orderResData, appConfig.getKey());
        return orderResData;
    }

    private void validateApiResponseData(String apiName, String orderServiceResponseString, WXPayData orderResData, String appKey) throws Exception {
        if (orderResData.getValue(WXPayConstants.returnCodeKey) == null) {
            logger.error("[" + apiName + "]Case1:API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问。");
            throw new KException("[" + apiName + "]API请求逻辑错误");
        }

        //Debug:查看数据是否正常被填充到scanPayResponseData这个对象中
        //Util.reflect(orderResData);

        if (orderResData.getValue(WXPayConstants.returnCodeKey).equals("FAIL")) {
            ///注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
            logger.error("[" + apiName + "]Case2:API系统返回失败，请检测Post给API的数据是否规范合法");
            throw new KException("[" + apiName + "]API系统返回失败");
        } else {
            logger.info("[" + apiName + "]API系统成功返回数据");
            //--------------------------------------------------------------------
            //收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
            //--------------------------------------------------------------------
            if (!Signature.checkIsSignValidFromResponseString(orderServiceResponseString, appKey)) {
                logger.error("[" + apiName + "]Case3:API返回的数据签名验证失败，有可能数据被篡改了");
                throw new KException("[" + apiName + "]API返回的数据签名验证失败");
            }
        }
    }

    private Long maxExpireTime(ThirdPartyRequestPayInfo requestPayInfo) {
        Long currentTime = Instant.now().getEpochSecond();
        Long leftTime = requestPayInfo.getExpireTime() - currentTime;
        return Math.max(5 * 60, leftTime) + currentTime;
    }

    private WXPerAppConfig extractWxConfig(PaymentAccountInfoInterface accountInfo) {
        // TODO: attachInfo怎么设置?
        return new WXPerAppConfig(accountInfo);
    }

}
