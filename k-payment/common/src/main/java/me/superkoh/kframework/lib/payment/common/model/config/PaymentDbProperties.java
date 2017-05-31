package me.superkoh.kframework.lib.payment.common.model.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库配置
 * Created by zhangyh on 2017/5/26.
 */
@Configuration
@ConfigurationProperties(prefix = PaymentDbProperties.PAYMENT_DB_PREFIX)
public class PaymentDbProperties {
    public static final String PAYMENT_DB_PREFIX = "kframework.payment.common.db";

    public static String PAYMENT_TRADE_TABLE     = "payment_transaction";
    public static String PAYMENT_EXCEPTION_TABLE = "payment_exception_log";

    private String paymentTable = "payment_transaction";
    private String paymentExceptionTable = "payment_exception_log";

    public String getPaymentTable() {
        return paymentTable;
    }

    public void setPaymentTable(String paymentTable) {
        this.paymentTable = paymentTable;
        PAYMENT_TRADE_TABLE = paymentTable;
    }

    public String getPaymentExceptionTable() {
        return paymentExceptionTable;
    }

    public void setPaymentExceptionTable(String paymentExceptionTable) {
        this.paymentExceptionTable = paymentExceptionTable;
        PAYMENT_EXCEPTION_TABLE = paymentExceptionTable;
    }
}
