package me.superkoh.kframework.lib.payment.common.service.impl;

import me.superkoh.kframework.core.exception.KException;
import me.superkoh.kframework.core.utils.ACU;
import me.superkoh.kframework.lib.payment.common.config.PaymentAccountInfo;
import me.superkoh.kframework.lib.payment.common.model.domain.PaymentExceptionLog;
import me.superkoh.kframework.lib.payment.common.model.domain.PaymentTransaction;
import me.superkoh.kframework.lib.payment.common.model.mapper.PaymentExceptionLogMapper;
import me.superkoh.kframework.lib.payment.common.model.mapper.PaymentTransactionMapper;
import me.superkoh.kframework.lib.payment.common.service.PaymentOrderCoordinator;
import me.superkoh.kframework.lib.payment.common.service.PaymentService;
import me.superkoh.kframework.lib.payment.common.service.ThirdPartyPayService;
import me.superkoh.kframework.lib.payment.common.service.info.*;
import me.superkoh.kframework.lib.payment.common.type.PaymentMethod;
import me.superkoh.kframework.lib.payment.common.type.PaymentServiceStatus;
import me.superkoh.kframework.lib.payment.common.type.PaymentStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支付服务实现
 * Created by zhangyh on 2016/10/10.
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentTransactionMapper transactionMapper;
    @Autowired
    private PaymentExceptionLogMapper exceptionLogMapper;
    @Autowired
    private PlatformTransactionManager txManager;

    @Override
    @Transactional(readOnly = true)
    public PaymentTransactionInfo getTransactionInfoById(String tradeId, Long userId) throws KException {
        tradeId = tradeId.replaceAll("^([A-Za-z]+)", "");
        PaymentTransaction transactionPO = transactionMapper.selectByPrimaryKey(Integer.valueOf(tradeId));
        if (null == transactionPO) {
            throw new KException("交易不存在");
        }
        return new PaymentTransactionInfo(transactionPO);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public PaymentPrepayInfo getPaymentPrepayInfo(PrepayRequestInfo reqInfo, PaymentAccountInfo accountInfo) throws Exception {
        if (reqInfo.getPayMethod().equals(PaymentMethod.OFFLINE_TRANSFER)) {
            return startOfflineTransfer(reqInfo, accountInfo);
        } else {
            return startOnlinePayment(reqInfo, accountInfo);
        }
    }

    @Override
    public PaymentStatusInfo queryPaymentState(PaymentTransactionInfo transactionInfo, PaymentAccountInfo accountInfo) throws Exception {
        ThirdPartyPayService payService = getPayServiceByMethod(transactionInfo.getPaymentMethod());
        PaymentStatusInfo statusInfo = payService.queryPayResult(transactionInfo.getId().toString(),
                transactionInfo.getTransactionTime(), accountInfo);
        if (statusInfo.getStatus().equals(PaymentStatus.NOT_PAY) && statusInfo.getUnionOrderNotExist()) {
            Long currentTime = Instant.now().getEpochSecond();
            if (currentTime > transactionInfo.getExpireTime() &&
                    currentTime > transactionInfo.getTransactionTime() + 10 * 60) {
                statusInfo.setStatus(PaymentStatus.CLOSED);
            }
        }
        updateTransactionOrderStatus(statusInfo);
        return statusInfo;
    }

    @Override
    public PaymentStatusInfo closePaymentTransaction(PaymentTransactionInfo transactionInfo, PaymentAccountInfo accountInfo) throws Exception {
        ThirdPartyPayService payService = getPayServiceByMethod(transactionInfo.getPaymentMethod());
        PaymentStatusInfo statusInfo =  payService.closeUnfinishedPay(transactionInfo.getId().toString(), accountInfo);
        if (statusInfo.getStatus().equals(PaymentStatus.NOT_PAY) && statusInfo.getUnionOrderNotExist()) {
            Long currentTime = Instant.now().getEpochSecond();
            if (currentTime > transactionInfo.getExpireTime() &&
                    currentTime > transactionInfo.getTransactionTime() + 10 * 60) {
                statusInfo.setStatus(PaymentStatus.CLOSED);
            }
        }
        updateTransactionOrderStatus(statusInfo);
        return statusInfo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentTransactionInfo> findTransactionsNeedQueryState() {
        Long minTime = Instant.now().getEpochSecond();
        Long maxTime = minTime - 5 * 60;
        String statusName = PaymentStatus.NOT_PAY.name();
        return transactionMapper.selectNeedQueryTransactions(maxTime, minTime, statusName)
                .stream().map(PaymentTransactionInfo::new).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<PaymentTransactionInfo>> findExpiredTransactions() {
        List<String> orderList = getOrderCoordinator().getExpiredOrderIdList();
        if (!orderList.isEmpty()) {
            Map<String, List<PaymentTransactionInfo>> transactionInfoMap =
                    transactionMapper.selectExpiredTransactions(orderList).stream()
                    .map(PaymentTransactionInfo::new)
                    .collect(Collectors.groupingBy(PaymentTransactionInfo::getOrderId));

            orderList.forEach(orderId -> transactionInfoMap.putIfAbsent(orderId, null));
            return transactionInfoMap;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentTransactionInfo> findTransactionsNeedClose() {
        return transactionMapper.selectNeedClosedTransactions().stream()
                .map(PaymentTransactionInfo::new).collect(Collectors.toList());
    }

    @Override
    public void cancelExpiredOrder(String orderId) throws KException {
        getOrderCoordinator().cancelOrder(orderId);
    }

    @Override
    public PaymentNotifyProcessInfo handleUnionpayBackNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfo accountInfo) throws Exception {
        ThirdPartyPayService unionService = getPayServiceByMethod(PaymentMethod.UNION_PC);
        PaymentNotifyProcessInfo notifyStatus = unionService.handleBackNotify(encoding, notifyParams, accountInfo);
        try {
            updateTransactionOrderStatus(notifyStatus.getStatusInfo());
        } catch (KException e) {
            notifyStatus = unionService.getFailedNotifyStatus("不存在的订单或金额错误");
        }
        return notifyStatus;
    }

    @Override
    public PaymentNotifyProcessInfo handleUnionpayFrontNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfo accountInfo) throws Exception {
        ThirdPartyPayService unionService = getPayServiceByMethod(PaymentMethod.UNION_PC);
        PaymentNotifyProcessInfo notifyStatus = unionService.handleFrontNotify(encoding, notifyParams, accountInfo);
        return notifyStatus;
    }

    @Override
    public PaymentNotifyProcessInfo handleWxpayBackNotify(String notifyString, PaymentAccountInfo accountInfo) throws Exception {
        ThirdPartyPayService wxService = getPayServiceByMethod(PaymentMethod.WEIXIN_JSAPI);
        Map<String, String> notifyParams = new HashMap<>();
        notifyParams.put("notifyStr", notifyString);
        PaymentNotifyProcessInfo notifyStatus = wxService.handleBackNotify(null, notifyParams, accountInfo);
        try {
            updateTransactionOrderStatus(notifyStatus.getStatusInfo());
        } catch (KException e) {
            notifyStatus = wxService.getFailedNotifyStatus("不存在的订单或金额错误");
        }
        return notifyStatus;
    }

    @Override
    public PaymentNotifyProcessInfo handleWxpayFrontNotify(String notifyString, PaymentAccountInfo accountInfo) throws Exception {
        ThirdPartyPayService wxService = getPayServiceByMethod(PaymentMethod.WEIXIN_JSAPI);
        Map<String, String> notifyParams = new HashMap<>();
        notifyParams.put("notifyStr", notifyString);
        PaymentNotifyProcessInfo notifyStatus = wxService.handleFrontNotify(null, notifyParams, accountInfo);
        return notifyStatus;
    }

    @Override
    public PaymentNotifyProcessInfo handleAlipayBackNotify(Map<String, String> notifyParams, PaymentAccountInfo accountInfo) throws Exception {
        ThirdPartyPayService alipayService = getPayServiceByMethod(PaymentMethod.ALIPAY_WAP);
        PaymentNotifyProcessInfo notifyStatus = alipayService.handleBackNotify(null, notifyParams, accountInfo);
        try {
            updateTransactionOrderStatus(notifyStatus.getStatusInfo());
        } catch (KException e) {
            notifyStatus = alipayService.getFailedNotifyStatus("不存在的订单或金额错误");
        }
        return notifyStatus;
    }

    @Override
    public PaymentNotifyProcessInfo handleAlipayFrontNotify(Map<String, String> notifyParams, PaymentAccountInfo accountInfo) throws Exception {
        ThirdPartyPayService alipayService = getPayServiceByMethod(PaymentMethod.ALIPAY_WAP);
        PaymentNotifyProcessInfo notifyStatus = alipayService.handleFrontNotify(null, notifyParams, accountInfo);
        return notifyStatus;
    }

    private PaymentPrepayInfo startOfflineTransfer(PrepayRequestInfo reqInfo, PaymentAccountInfo accountInfo) throws KException {
        PaymentOrderInfo orderInfo = getOrderCoordinator().getPaymentOrderInfoById(reqInfo.getOrderId(), reqInfo.getUserId
                ());
        if (!orderInfo.getOfflineTransfer()) {
            getOrderCoordinator().fixOrderToOfflineTransfer(reqInfo.getOrderId());
            updateTransactionsToNeedCloseByOrder(reqInfo.getOrderId(), 0);
        }
        return new PaymentPrepayInfo();
    }

    private PaymentPrepayInfo startOnlinePayment(PrepayRequestInfo reqInfo, PaymentAccountInfo accountInfo) throws Exception {
        ThirdPartyPayService payService = getPayServiceByMethod(reqInfo.getPayMethod());
        PaymentOrderInfo orderInfo = createPaymentTransaction(reqInfo);
        if (null == orderInfo) {
            throw new KException("交易创建失败");
        }

        ThirdPartyRequestPayInfo requestPayInfo = new ThirdPartyRequestPayInfo(orderInfo, reqInfo);
        return payService.getPrepayInfo(requestPayInfo, accountInfo);
    }

    private ThirdPartyPayService getPayServiceByMethod(PaymentMethod method) throws KException {
        ThirdPartyPayService payService = null;
        switch (method) {
            case UNION_PC:
            case UNION_WAP:
                payService = (ThirdPartyPayService) ACU.bean("Unionpay");
                break;
            case WEIXIN_JSAPI:
            case WEIXIN_NATIVE:
                payService = (ThirdPartyPayService) ACU.bean("WxPay");
                break;
            case ALIPAY_DIRECT:
            case ALIPAY_WAP:
                payService = (ThirdPartyPayService) ACU.bean("Alipay");
                break;
        }

        if (null == payService) {
            throw new KException("暂不支持的支付方式! [" + method.name() + "]");
        }
        return payService;
    }

    private PaymentOrderInfo createPaymentTransaction(PrepayRequestInfo reqInfo) throws KException {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);

        TransactionStatus status = txManager.getTransaction(def);
        PaymentOrderInfo orderInfo;
        try {
            orderInfo = getOrderCoordinator().getPaymentOrderInfoById(reqInfo.getOrderId(), reqInfo.getUserId());
            if (orderInfo.getOfflineTransfer()) {
                throw new KException("已经选择线下支付，请使用线下支付");
            }

            List<PaymentTransaction> checkPOList = transactionMapper.selectByOrderIdAndPayMethod(reqInfo.getOrderId(), reqInfo.getPayMethod().name());
            if (checkPOList.size() == 0 ||
                    (checkPOList.get(0).getStatus().equals(PaymentStatus.CLOSED.name())) ||
                    (checkPOList.get(0).getStatus().equals(PaymentStatus.PAY_ERROR.name()))) {
                PaymentTransaction transactionPO = new PaymentTransaction();
                BeanUtils.copyProperties(orderInfo, transactionPO);
                transactionPO.setNeedClose(false);
                transactionPO.setStatus(PaymentStatus.NOT_PAY.name());
                transactionPO.setTimezone("Asia/Shanghai");
                transactionPO.setPaymentMethod(reqInfo.getPayMethod().name());
                transactionPO.setAmount(orderInfo.getAmount().multiply(BigDecimal.valueOf(100.0)).intValue());
                transactionMapper.insert(transactionPO);
                orderInfo.setTradeId(transactionPO.getId());
            } else {
                PaymentTransaction checkPO = checkPOList.get(0);
                if (checkPO.getStatus().equals(PaymentStatus.SUCCESS.name()) ||
                        checkPO.getStatus().equals(PaymentStatus.CLOSED.name())) {
                    throw new KException("订单已被支付");
                }

                transactionMapper.updateToNotPay(reqInfo.getOrderId(), reqInfo.getPayMethod().name(),
                        orderInfo.getTransactionTime(), PaymentStatus.NOT_PAY.name());
                orderInfo.setTradeId(checkPO.getId());
            }
        } catch (Exception e) {
            txManager.rollback(status);
            throw e;
        }
        txManager.commit(status);
        return orderInfo;
    }

    private void updateTransactionOrderStatus(PaymentStatusInfo statusInfo) throws KException {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        TransactionStatus status = txManager.getTransaction(def);
        try {
            if (statusInfo.getServiceStatus().equals(PaymentServiceStatus.SUCCESS)) {
                Integer tradeId = Integer.valueOf(statusInfo.getTradeId());
                PaymentTransaction transactionPO = transactionMapper.selectByPrimaryKey(tradeId);
                if (null == transactionPO) {
                    throw new KException("找不到对应交易号");
                }

                PaymentStatus localStatus = PaymentStatus.valueOf(transactionPO.getStatus());
                PaymentStatus remoteStatus = statusInfo.getStatus();
                if (remoteStatus.equals(PaymentStatus.SUCCESS)) {
                    String amount;
                    if (statusInfo.getAmountUnitIsYuan()) {
                        amount = String.format("%.2f", transactionPO.getAmount() / 100.0);
                    } else {
                        amount = transactionPO.getAmount().toString();
                    }
                    if (!amount.equals(statusInfo.getTotalAmount())) {
                        throw new KException("订单总价不匹配");
                    }
                }

                if (!localStatus.equals(remoteStatus)) {
                    if (localStatus.equals(PaymentStatus.NOT_PAY) || remoteStatus.equals(PaymentStatus.SUCCESS)) {
                        transactionPO.setStatus(remoteStatus.name());
                        switch (remoteStatus) {
                            case SUCCESS:
                                transactionPO.setPaymentTime(statusInfo.getPaymentTime());
                                updateTransactionsToNeedCloseByOrder(transactionPO.getOrderId(), transactionPO.getId());
                                getOrderCoordinator().paymentSuccess(transactionPO.getOrderId(), transactionPO);
                                break;
                            case CLOSED:
                            case PAY_ERROR:
                                break;
                        }
                        transactionMapper.updateByPrimaryKeySelective(transactionPO);
                    } else {
                        logPaymentException(transactionPO.getOrderId(), tradeId, localStatus.name(), remoteStatus.name(), "交易状态错误");
                    }
                }
            }
        } catch (Exception e) {
            txManager.rollback(status);
            throw e;
        }
        txManager.commit(status);
    }

    private void updateTransactionsToNeedCloseByOrder(String orderId, Integer exceptTradeId) {
        transactionMapper.updateToNeedCloseByOrderIdAndExceptTradeId(orderId, exceptTradeId);
    }

    private void logPaymentException(String orderId, Integer tradeId, String prevStatus, String currentStatus, String description) {
        try {
            PaymentExceptionLog exceptionLogPO = new PaymentExceptionLog();
            exceptionLogPO.setOrderId(orderId);
            exceptionLogPO.setTransactionId(tradeId);
            exceptionLogPO.setTime(Instant.now().getEpochSecond());
            exceptionLogPO.setPrevStatus(prevStatus);
            exceptionLogPO.setDescription(description);
            exceptionLogPO.setCurrentStatus(currentStatus);
            exceptionLogMapper.insert(exceptionLogPO);
        } catch (Exception ignored) {
        }
    }

    private PaymentOrderCoordinator getOrderCoordinator() {
        return (PaymentOrderCoordinator) ACU.bean("PaymentOrderCoordinator");
    }
}
