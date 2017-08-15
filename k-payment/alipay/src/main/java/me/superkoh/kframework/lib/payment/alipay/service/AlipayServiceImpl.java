package me.superkoh.kframework.lib.payment.alipay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import me.superkoh.kframework.core.utils.DateTimeHelper;
import me.superkoh.kframework.lib.payment.alipay.sdk.config.AlipayConfig;
import me.superkoh.kframework.lib.payment.alipay.sdk.util.AlipaySubmit;
import me.superkoh.kframework.lib.payment.common.config.PaymentAccountInfoInterface;
import me.superkoh.kframework.lib.payment.common.service.ThirdPartyPayService;
import me.superkoh.kframework.lib.payment.common.service.info.*;
import me.superkoh.kframework.lib.payment.common.type.PaymentChannel;
import me.superkoh.kframework.lib.payment.common.type.PaymentServiceStatus;
import me.superkoh.kframework.lib.payment.common.type.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝支付服务
 * Created by zhangyh on 16/9/22.
 */
@Service(value = "Alipay")
public class AlipayServiceImpl implements ThirdPartyPayService {
    private static final Logger logger = LoggerFactory.getLogger("paymentLogger");

    @Override
    public PaymentPrepayInfo getPrepayInfo(ThirdPartyRequestPayInfo requestPayInfo, PaymentAccountInfoInterface accountInfo) throws Exception {
        String amount = String.format("%.2f", requestPayInfo.getAmount() / 100.0);
        String timeout = getTimeoutString(requestPayInfo);
        String tradeNo = accountInfo.getTradePrefix() + requestPayInfo.getTradeId();
        String productName = requestPayInfo.getProductName();
        String productDesc = requestPayInfo.getProductDesc();
        AlipayPrepayInfo alipayPrepayInfo;
        AlipayConfig config = extraAlipayConfig(accountInfo);

        if (requestPayInfo.getChannel().equals(PaymentChannel.APP)) {
            alipayPrepayInfo = getAlipayAppPayInfo(tradeNo, amount, timeout, productName, productDesc, accountInfo);
        } else if (requestPayInfo.getChannel().equals(PaymentChannel.WAP)) {
            alipayPrepayInfo = getAlipayWapPayInfo(tradeNo, amount, timeout, productName, productDesc, accountInfo);
        } else {
            alipayPrepayInfo = getAlipayDirectPayInfo(tradeNo, amount, timeout, productName, productDesc, config);
        }

        PaymentPrepayInfo paymentPrepayInfo = new PaymentPrepayInfo();
        paymentPrepayInfo.setAlipay(alipayPrepayInfo);
        return paymentPrepayInfo;
    }

    @Override
    public PaymentStatusInfo queryPayResult(String tradeId, Long tradeTime, PaymentAccountInfoInterface accountInfo) throws Exception {
        tradeId =  accountInfo.getTradePrefix() + tradeId;
        AlipayTradeQueryResponse response = queryAlipayWapPay(tradeId, accountInfo);
        PaymentStatusInfo statusInfo = new PaymentStatusInfo();
        statusInfo.setTradeIdWithPrefix(tradeId,  accountInfo.getTradePrefix());

        if (null == response) {
            statusInfo.setServiceStatus(PaymentServiceStatus.NETWORK_ERROR);
            statusInfo.setServiceErrorDesc("网络错误");
        } else if (response.isSuccess()) {
            statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);
            statusInfo.setErrorDesc(response.getTradeStatus());
            switch (response.getTradeStatus()) {
                case "WAIT_BUYER_PAY":
                    statusInfo.setStatus(PaymentStatus.NOT_PAY);
                    break;
                case "TRADE_CLOSED":
                    statusInfo.setStatus(PaymentStatus.CLOSED);
                    break;
                case "TRADE_SUCCESS":
                    statusInfo.setTradeIdWithPrefix(response.getOutTradeNo(),  accountInfo.getTradePrefix());
                    statusInfo.setTotalAmount(response.getTotalAmount());
                    statusInfo.setAmountUnitIsYuan(true);
                    if (null != response.getSendPayDate()) {
                        Long payTime = response.getSendPayDate().toInstant().atZone(ZoneId.of("Asia/Shanghai")).toEpochSecond();
                        statusInfo.setPaymentTime(payTime);
                    } else {
                        statusInfo.setPaymentTime(Instant.now().getEpochSecond());
                    }
                    statusInfo.setStatus(PaymentStatus.SUCCESS);
                    break;
                case "TRADE_FINISHED":
                    statusInfo.setStatus(PaymentStatus.SUCCESS);
                    break;
                default:
                    statusInfo.setStatus(PaymentStatus.NOT_PAY);
                    break;
            }
        } else {
            if (response.getSubCode().equals("ACQ.TRADE_NOT_EXIST")) {
                statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);
                statusInfo.setStatus(PaymentStatus.CLOSED);
            } else {
                statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
                statusInfo.setServiceErrorCode(response.getSubCode());
                statusInfo.setServiceErrorDesc(response.getSubMsg());
            }
        }

        return statusInfo;
    }

    @Override
    public PaymentStatusInfo closeUnfinishedPay(String tradeId, PaymentAccountInfoInterface accountInfo) throws Exception {
        tradeId =  accountInfo.getTradePrefix() + tradeId;
        AlipayTradeCloseResponse response = closeAlipayUnfinishedPay(tradeId, accountInfo);
        PaymentStatusInfo statusInfo = new PaymentStatusInfo();
        statusInfo.setTradeIdWithPrefix(tradeId,  accountInfo.getTradePrefix());

        if (null == response) {
            statusInfo.setServiceStatus(PaymentServiceStatus.NETWORK_ERROR);
            statusInfo.setServiceErrorDesc("网络错误");
        } else if (response.isSuccess()) {
            statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);
            statusInfo.setStatus(PaymentStatus.CLOSED);
        } else {
            if (response.getSubCode().equals("ACQ.TRADE_NOT_EXIST")) {
                statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);
                statusInfo.setStatus(PaymentStatus.CLOSED);
            } else if (response.getSubCode().equals("ACQ.TRADE_STATUS_ERROR")) {
                statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
                statusInfo.setServiceErrorCode(response.getSubCode());
                statusInfo.setServiceErrorDesc(response.getSubMsg());
            } else {
                statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
                statusInfo.setServiceErrorCode(response.getSubCode());
                statusInfo.setServiceErrorDesc(response.getSubMsg());
            }
        }

        return statusInfo;
    }

    @Override
    public PaymentNotifyProcessInfo handleBackNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfoInterface accountInfo) throws Exception {
        PaymentNotifyProcessInfo notifyStatus = new PaymentNotifyProcessInfo();
        notifyStatus.setResponseEncoding("UTF-8");
        notifyStatus.setResponseContentType("text/html;charset=UTF-8");
        AlipayConfig config = extraAlipayConfig(accountInfo);

        logger.info("收到支付宝支付结果通知: " + notifyParams.toString());
        Map<String, String> tempParams = new HashMap<>();
        tempParams.putAll(notifyParams);

        Boolean rsaCheckValid;
        if ("RSA2".equals(config.getSignType())) {
            rsaCheckValid = AlipaySignature.rsaCheckV1(notifyParams, config.getPublicKey(), "utf-8", config.getSignType());
        } else {
            rsaCheckValid = AlipaySignature.rsaCheckV1(notifyParams, config.getPublicKey(), "utf-8", config.getSignType());
        }

        if (rsaCheckValid) {
            logger.info("支付宝结果通知签名验证通过");

            String sellerId = notifyParams.get("seller_id");
            String appId = notifyParams.get("app_id");

            if (!config.getPartnerId().equals(sellerId) ||
                    (null != appId && !appId.isEmpty() && !accountInfo.getAliAppId().equals(appId))) {
                notifyStatus.getStatusInfo().setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
                notifyStatus.setResponseBody("failure");
                logger.info("支付宝后台通知错误，sellId或者appId不匹配");
            } else {
                String outTradeNo = notifyParams.get("out_trade_no");
                String tradeStatus = notifyParams.get("trade_status");
                String totalAmount = notifyParams.get("total_amount");
                if (null == totalAmount || totalAmount.isEmpty()) {
                    totalAmount = notifyParams.get("total_fee");
                }
                String paymentTime = notifyParams.get("gmt_payment");

                notifyStatus.getStatusInfo().setServiceStatus(PaymentServiceStatus.SUCCESS);
                notifyStatus.setResponseBody("success");
                notifyStatus.getStatusInfo().setTradeIdWithPrefix(outTradeNo,  accountInfo.getTradePrefix());
                notifyStatus.getStatusInfo().setTotalAmount(totalAmount);
                notifyStatus.getStatusInfo().setAmountUnitIsYuan(true);

                switch (tradeStatus) {
                    case "WAIT_BUYER_PAY":
                        notifyStatus.getStatusInfo().setStatus(PaymentStatus.NOT_PAY);
                        break;
                    case "TRADE_CLOSED":
                        notifyStatus.getStatusInfo().setStatus(PaymentStatus.CLOSED);
                        break;
                    case "TRADE_SUCCESS":
                    case "TRADE_FINISHED":
                        if (null != paymentTime && !paymentTime.isEmpty()) {
                            notifyStatus.getStatusInfo().setPaymentTime(DateTimeHelper.timestampOfDateTimeStringAtChina(paymentTime, "yyyy-MM-dd HH:mm:ss"));
                        } else {
                            notifyStatus.getStatusInfo().setPaymentTime(Instant.now().getEpochSecond());
                        }
                        notifyStatus.getStatusInfo().setStatus(PaymentStatus.SUCCESS);
                        break;
                    default:
                        notifyStatus.getStatusInfo().setStatus(PaymentStatus.NOT_PAY);
                        break;
                }
                logger.info("支付宝后台通知成功, 交易状态: " + tradeStatus);
            }
        } else {
            notifyStatus.getStatusInfo().setServiceStatus(PaymentServiceStatus.SIGN_ERROR);
            notifyStatus.setResponseBody("failure");
            logger.error("支付宝结果通知签名验证失败");
        }

        return notifyStatus;
    }

    @Override
    public PaymentNotifyProcessInfo handleFrontNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfoInterface accountInfo) throws Exception {
        return null;
    }

    @Override
    public PaymentNotifyProcessInfo getFailedNotifyStatus(String message) throws Exception {
        PaymentNotifyProcessInfo notifyStatus = new PaymentNotifyProcessInfo();
        notifyStatus.getStatusInfo().setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
        notifyStatus.setResponseEncoding("UTF-8");
        notifyStatus.setResponseContentType("text/html;charset=UTF-8");
        notifyStatus.setResponseBody("failure");
        return notifyStatus;
    }

    @Override
    public PaymentStatusInfo queryRefundState(String tradeId, PaymentAccountInfoInterface accountInfo) throws Exception {
        return null;
    }

    @Override
    public PaymentStatusInfo applyRefund(String tradeId, int totalFee, PaymentAccountInfoInterface accountInfo) throws Exception {
        return null;
    }

    private AlipayPrepayInfo getAlipayAppPayInfo(String tradeNo, String amount, String timeout, String productName, String productDesc, PaymentAccountInfoInterface accountInfo) {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(productDesc);
        model.setSubject(productName);
        model.setOutTradeNo(tradeNo);
        model.setTimeoutExpress(timeout);
        model.setTotalAmount(amount);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(accountInfo.getAliNotifyUrl());

        //实例化客户端
        AlipayClient alipayClient = getAlipayClient(accountInfo);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return new AlipayPrepayInfo(null, response.getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
            logger.error("Alipay get app pay info failed!");
        }
        return null;
    }

    private AlipayPrepayInfo getAlipayDirectPayInfo(String tradeNo, String amount, String timeout, String productName, String productDesc, AlipayConfig config)
            throws Exception {
        Map<String, String> sParaTemp = new HashMap<>();
        sParaTemp.put("service", config.getService());
        sParaTemp.put("partner", config.getPartnerId());
        sParaTemp.put("_input_charset", AlipayConfig.inputCharset);
        sParaTemp.put("notify_url", config.getNotifyUrl());
        sParaTemp.put("return_url", config.getReturnUrl());
        sParaTemp.put("anti_phishing_key", config.getAntiPhishingKey());
        sParaTemp.put("exter_invoke_ip", config.getExterInvokeIp());

        sParaTemp.put("out_trade_no", tradeNo);
        sParaTemp.put("subject", productName);
        sParaTemp.put("payment_type", config.getPaymentType());
        sParaTemp.put("total_fee", amount);
        sParaTemp.put("seller_id", config.getSellerId());
        sParaTemp.put("body", productDesc);
        sParaTemp.put("it_b_pay", timeout);

        String form = AlipaySubmit.buildRequest(sParaTemp, "get", "立即支付", config);
        return new AlipayPrepayInfo(form, null);
    }

    private AlipayPrepayInfo getAlipayWapPayInfo(String tradeNo, String amount, String timeout, String productName, String productDesc, PaymentAccountInfoInterface accountInfo)
            throws Exception {
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl(accountInfo.getAliReturnUrl());
        alipayRequest.setNotifyUrl(accountInfo.getAliNotifyUrl());
        alipayRequest.setBizContent("{" +
                "\"out_trade_no\":\"" + tradeNo + "\"," +
                "\"total_amount\":" + amount + "," +
                "\"seller_id\":\"" + accountInfo.getAliPartnerId() + "\"," +
                "\"subject\":\"" + productName + "\"," +
                "\"body\":\"" + productDesc + "\"," +
                "\"timeout_express\":\"" + timeout + "\"," +
                "\"product_code\":\"QUICK_WAP_PAY\"" +
                "}");

        String form = getAlipayClient(accountInfo).pageExecute(alipayRequest).getBody();
        return new AlipayPrepayInfo(form, null);
    }

    private AlipayTradeQueryResponse queryAlipayWapPay(String tradeNo, PaymentAccountInfoInterface accountInfo) throws Exception {
        AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest();
        queryRequest.setBizContent("{" +
                "\"out_trade_no\":\"" + tradeNo + "\"" +
                "}");
       return getAlipayClient(accountInfo).execute(queryRequest);
    }

    private AlipayTradeCloseResponse closeAlipayUnfinishedPay(String tradeNo, PaymentAccountInfoInterface accountInfo) throws Exception {
        AlipayTradeCloseRequest closeRequest = new AlipayTradeCloseRequest();
        closeRequest.setBizContent("{" +
                "\"out_trade_no\":\"" + tradeNo + "\"," +
                "\"operator_id\":\"SYSTEM_CANCEL\"" +
                "}");
        return getAlipayClient(accountInfo).execute(closeRequest);
    }

    private String getTimeoutString(ThirdPartyRequestPayInfo requestPayInfo) {
        Long leftTime = requestPayInfo.getExpireTime() - Instant.now().getEpochSecond();
        leftTime = leftTime / 60;
        leftTime = Math.max(1, leftTime);
        return String.format("%dm", leftTime);
    }

    private AlipayConfig extraAlipayConfig(PaymentAccountInfoInterface accountInfo) {
        AlipayConfig config = new AlipayConfig();
        config.setPartnerId(accountInfo.getAliPartnerId());
        config.setSellerId(accountInfo.getAliPartnerId());
        config.setPrivateKey(accountInfo.getAliAppPrivateKey());
        config.setPublicKey(accountInfo.getAliAppPublicKey());
        config.setNotifyUrl(accountInfo.getAliNotifyUrl());
        config.setReturnUrl(accountInfo.getAliReturnUrl());
        config.setSignType(accountInfo.getAliSignType());
        return config;
    }

    private AlipayClient getAlipayClient(PaymentAccountInfoInterface accountInfo) {
        return new DefaultAlipayClient(accountInfo.getAliServerUrl(),
                accountInfo.getAliAppId(), accountInfo.getAliAppPrivateKey(), "json",
                "utf-8", accountInfo.getAliAppPublicKey(), accountInfo.getAliSignType());
    }
}
