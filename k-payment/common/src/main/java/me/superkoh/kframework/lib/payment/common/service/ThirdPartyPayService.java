package me.superkoh.kframework.lib.payment.common.service;

import me.superkoh.kframework.lib.payment.common.config.PaymentAccountInfoInterface;
import me.superkoh.kframework.lib.payment.common.service.info.PaymentNotifyProcessInfo;
import me.superkoh.kframework.lib.payment.common.service.info.PaymentPrepayInfo;
import me.superkoh.kframework.lib.payment.common.service.info.PaymentStatusInfo;
import me.superkoh.kframework.lib.payment.common.service.info.ThirdPartyRequestPayInfo;

import java.util.Map;

/**
 * 第三方支付服务
 * Created by zhangyh on 2017/5/23.
 */
public interface ThirdPartyPayService {
    PaymentPrepayInfo getPrepayInfo(ThirdPartyRequestPayInfo requestPayInfo, PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentStatusInfo queryPayResult(String tradeId, Long tradeTime, PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentStatusInfo closeUnfinishedPay(String tradeId, PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentStatusInfo queryRefundState(String tradeId, PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentStatusInfo applyRefund(String tradeId, int totalFee, PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentStatusInfo applyRefund(String tradeId, int totalFee, int refundFee, PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentNotifyProcessInfo handleBackNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentNotifyProcessInfo handleFrontNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfoInterface accountInfo) throws Exception;

    PaymentNotifyProcessInfo getFailedNotifyStatus(String message) throws Exception;
}
