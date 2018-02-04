package me.superkoh.kframework.lib.payment.common.service.impl;

import me.superkoh.kframework.core.exception.KException;
import me.superkoh.kframework.core.utils.ACU;
import me.superkoh.kframework.lib.payment.common.config.PaymentAccountInfoInterface;
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
    public PaymentPrepayInfo getPaymentPrepayInfoForWeb(PrepayRequestInfo reqInfo, PaymentAccountInfoInterface accountInfo) throws Exception {
        if (reqInfo.getPayMethod().equals(PaymentMethod.OFFLINE_TRANSFER)) {
            return startOfflineTransfer(reqInfo.getOrderId(), reqInfo.getUserId());
        } else {
            return startOnlinePayment(reqInfo, accountInfo);
        }
    }

    @Override
    public PaymentPrepayInfo getPaymentPrepayInfoForApp(PrepayRequestInfo reqInfo, PaymentAccountInfoInterface accountInfo) throws Exception {
        if (reqInfo.getPayMethod().equals(PaymentMethod.OFFLINE_TRANSFER)) {
            getOrderCoordinator().getPaymentOrderInfoById(reqInfo.getOrderId(), reqInfo.getUserId());
            return new PaymentPrepayInfo();
        } else {
            return startOnlinePayment(reqInfo, accountInfo);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void fixedOrderToOfflineTransfer(String orderId, String userId) throws Exception {
        startOfflineTransfer(orderId, userId);
    }

    @Override
    public PaymentStatusInfo queryPaymentState(PaymentTransactionInfo transactionInfo, PaymentAccountInfoInterface accountInfo) throws Exception {
        ThirdPartyPayService payService = getPayServiceByMethod(transactionInfo.getPaymentMethod());
        PaymentStatusInfo statusInfo = payService.queryPayResult(transactionInfo.getId().toString(),
                transactionInfo.getTransactionTime(), accountInfo);
        if (statusInfo.getServiceStatus() == PaymentServiceStatus.SUCCESS &&
                statusInfo.getStatus().equals(PaymentStatus.NOT_PAY) &&
                statusInfo.getUnionOrderNotExist()) {
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
    public PaymentStatusInfo closePaymentTransaction(PaymentTransactionInfo transactionInfo, PaymentAccountInfoInterface accountInfo) throws Exception {
        ThirdPartyPayService payService = getPayServiceByMethod(transactionInfo.getPaymentMethod());
        PaymentStatusInfo statusInfo =  payService.closeUnfinishedPay(transactionInfo.getId().toString(), accountInfo);
        if (statusInfo.getServiceStatus() == PaymentServiceStatus.SUCCESS &&
                statusInfo.getStatus().equals(PaymentStatus.NOT_PAY) &&
                statusInfo.getUnionOrderNotExist()) {
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
    public void updateRefundAmount(String orderId, int refundFee) throws Exception {
        transactionMapper.updateRefundAmountByOrderId(orderId, refundFee);
    }

    @Override
    public PaymentStatusInfo queryRefundState(PaymentTransactionInfo transactionInfo, PaymentAccountInfoInterface accountInfo) throws Exception {
        ThirdPartyPayService payService = getPayServiceByMethod(transactionInfo.getPaymentMethod());
        PaymentStatusInfo statusInfo = payService.queryRefundState(transactionInfo.getId().toString(), accountInfo);
        updateTransactionOrderRefundStatus(statusInfo);
        return statusInfo;
    }

    @Override
    public PaymentStatusInfo applyRefundTransaction(PaymentTransactionInfo transactionInfo, PaymentAccountInfoInterface accountInfo) throws Exception {
        ThirdPartyPayService payService = getPayServiceByMethod(transactionInfo.getPaymentMethod());
        PaymentStatusInfo statusInfo = payService.applyRefund(transactionInfo.getId().toString(),
                transactionInfo.getAmount(),transactionInfo.getRefundAmount(), accountInfo);
        updateTransactionOrderRefundStatus(statusInfo);
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
    @Transactional(readOnly = true)
    public List<PaymentTransactionInfo> findTransactionsNeedApplyRefund() {
        List<String> orderList = getOrderCoordinator().getUserAppliedRefundOrderIdList();
        if (!orderList.isEmpty()) {
            return transactionMapper.selectCanApplyRefundTransactions(PaymentStatus.SUCCESS.name(), orderList)
                    .stream()
                    .map(PaymentTransactionInfo::new)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentTransactionInfo> findTransactionsNeedQueryRefundState() {
        return transactionMapper.selectNeedQueryRefundStateTransactions(PaymentStatus.APPLIED_REFUND.name())
                .stream()
                .map(PaymentTransactionInfo::new)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelExpiredOrder(String orderId) throws Exception {
        getOrderCoordinator().cancelOrder(orderId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void modifyTransactionAmount(String orderId, BigDecimal amount) throws Exception {
        int fenAmount = amount.multiply(BigDecimal.valueOf(100.0)).intValue();
        if (fenAmount <= 0) {
            throw new KException("金额必须大于0！");
        }
        transactionMapper.updateToNeedCloseAndPayError(orderId);
//        transactionMapper.updateAmountByOrderId(orderId, fenAmount);
    }

    @Override
    public PaymentNotifyProcessInfo handleUnionpayBackNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfoInterface accountInfo) throws Exception {
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
    public PaymentNotifyProcessInfo handleUnionpayFrontNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfoInterface accountInfo) throws Exception {
        ThirdPartyPayService unionService = getPayServiceByMethod(PaymentMethod.UNION_PC);
        return unionService.handleFrontNotify(encoding, notifyParams, accountInfo);
    }

    @Override
    public PaymentNotifyProcessInfo handleWxpayBackNotify(String notifyString, PaymentAccountInfoInterface accountInfo) throws Exception {
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
    public PaymentNotifyProcessInfo handleWxpayFrontNotify(String notifyString, PaymentAccountInfoInterface accountInfo) throws Exception {
        ThirdPartyPayService wxService = getPayServiceByMethod(PaymentMethod.WEIXIN_JSAPI);
        Map<String, String> notifyParams = new HashMap<>();
        notifyParams.put("notifyStr", notifyString);
        return wxService.handleFrontNotify(null, notifyParams, accountInfo);
    }

    @Override
    public PaymentNotifyProcessInfo handleAlipayBackNotify(Map<String, String> notifyParams, PaymentAccountInfoInterface accountInfo) throws Exception {
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
    public PaymentNotifyProcessInfo handleAlipayFrontNotify(Map<String, String> notifyParams, PaymentAccountInfoInterface accountInfo) throws Exception {
        ThirdPartyPayService alipayService = getPayServiceByMethod(PaymentMethod.ALIPAY_WAP);
        return alipayService.handleFrontNotify(null, notifyParams, accountInfo);
    }

    private PaymentPrepayInfo startOfflineTransfer(String orderId, String userId) throws Exception {
        PaymentOrderInfo orderInfo = getOrderCoordinator().getPaymentOrderInfoById(orderId, userId);
        if (!orderInfo.getOfflineTransfer()) {
            getOrderCoordinator().fixOrderToOfflineTransfer(orderId);
            updateTransactionsToNeedCloseByOrder(orderId, 0);
        }
        return new PaymentPrepayInfo();
    }

    private PaymentPrepayInfo startOnlinePayment(PrepayRequestInfo reqInfo, PaymentAccountInfoInterface accountInfo) throws Exception {
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
                payService = ACU.ctx().getBean("Unionpay", ThirdPartyPayService.class);
                break;
            case WEIXIN_JSAPI:
            case WEIXIN_NATIVE:
            case WEIXIN_APP:
                payService = ACU.ctx().getBean("WxPay", ThirdPartyPayService.class);
                break;
            case ALIPAY_DIRECT:
            case ALIPAY_WAP:
            case ALIPAY_APP:
                payService = ACU.ctx().getBean("Alipay", ThirdPartyPayService.class);
                break;
        }

        if (null == payService) {
            throw new KException("暂不支持的支付方式! [" + method.name() + "]");
        }
        return payService;
    }

    private PaymentOrderInfo createPaymentTransaction(PrepayRequestInfo reqInfo) throws Exception {
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
                transactionPO.setOrderId(reqInfo.getOrderId());
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

    private void updateTransactionOrderStatus(PaymentStatusInfo statusInfo) throws Exception {
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
                    int needAmount = transactionPO.getAmount();
                    int payAmount;
                    if (statusInfo.getAmountUnitIsYuan()) {
                        payAmount = (int)(Float.valueOf(statusInfo.getTotalAmount()) * 100);
                    } else {
                        payAmount = Integer.valueOf(statusInfo.getTotalAmount());
                    }
                    if (payAmount + 5 < needAmount) {
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

    private void updateTransactionOrderRefundStatus(PaymentStatusInfo statusInfo) throws Exception {
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

                if (!localStatus.equals(remoteStatus)) {
                    boolean validState = (localStatus.equals(PaymentStatus.SUCCESS) &&
                            remoteStatus.equals(PaymentStatus.APPLIED_REFUND));
                    validState |=  (localStatus.equals(PaymentStatus.APPLIED_REFUND) &&
                            (remoteStatus.equals(PaymentStatus.REFUNDED) ||
                                    remoteStatus.equals(PaymentStatus.REFUND_FAILED)));

                    if (validState) {
                        transactionPO.setStatus(remoteStatus.name());
                        transactionPO.setRefundTime(statusInfo.getPaymentTime());
                        getOrderCoordinator().updateRefundStatus(transactionPO.getOrderId(), transactionPO, statusInfo.getRefundAmount());
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
