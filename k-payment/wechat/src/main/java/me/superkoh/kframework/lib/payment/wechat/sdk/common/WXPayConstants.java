package me.superkoh.kframework.lib.payment.wechat.sdk.common;

/**
 * Created by zhangyh on 16/9/18.
 */
public class WXPayConstants {
    //协议层
    public static final String returnCodeKey = "return_code";
    public static final String returnMsgKey = "return_msg";

    //协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
    public static final String appIdKey = "appid";
    public static final String mchIdKey = "mch_id";
    public static final String nonceStrKey = "nonce_str";
    public static final String signKey = "sign";
    public static final String resultCodeKey = "result_code";
    public static final String errCodeKey = "err_code";
    public static final String errCodeDesKey = "err_code_des";

    public static final String deviceInfoKey = "device_info";

    //业务返回的具体数据
    //交易结果通知
    public static final String openIdKey = "openid";
    public static final String isSubscribeKey = "is_subscribe";
    public static final String tradeTypeKey = "trade_type";
    public static final String bankTypeKey = "bank_type";
    public static final String totalFeeKey = "total_fee";
    public static final String settlementTotalKey = "settlement_total";
    public static final String feeTypeKey = "fee_type";
    public static final String cashFeeKey = "cash_fee";
    public static final String cashFeeTypeKey = "cash_fee_type";
    public static final String couponFeeKey = "coupon_fee";
    public static final String couponCountKey = "coupon_count";
    public static final String couponTypePrefixKey = "coupon_type_";
    public static final String couponIdPrefixKey = "coupon_id_";
    public static final String couponFeePrefixKey = "coupon_fee_";
    public static final String transactionIdKey = "transaction_id";
    public static final String outTradeNoKey = "out_trade_no";
    public static final String attachKey = "attach";
    public static final String timeEndKey = "time_end";

    //统一下单响应
    public static final String bodyKey = "body";
    public static final String detailKey = "detail";
    public static final String spbillCreateIpKey = "spbill_create_ip";
    public static final String timeStartKey = "time_start";
    public static final String timeExpireKey = "time_expire";
    public static final String goodsTagKey = "goods_tag";
    public static final String notifyUrlKey = "notify_url";
    public static final String productIdKey = "product_id";
    public static final String limitPayKey = "limit_pay";

    public static final String prepayIdKey = "prepay_id";
    public static final String codeUrlKey = "code_url";

    //扫码支付
    public static final String authCodeKey = "auth_code";
    public static final String recallKey = "recall";

    //查询订单
    public static final String tradeStateKey = "trade_state";
    public static final String tradeStateDescKey = "trade_state_desc";
    public static final String couponBatchIdPrefixKey = "coupon_batch_id_";

    //申请退款
    public static final String refundFeeTypeKey = "refund_fee_type";
    public static final String opUserIdKey = "op_user_id";
    public static final String refundAccountKey = "refund_account";

    public static final String outRefundNoKey = "out_refund_no";
    public static final String refundIdKey = "refund_id";
    public static final String refundChannelKey = "refund_channel";
    public static final String refundFeeKey = "refund_fee";
    public static final String settlementRefundFeeKey = "settlement_refund_fee";
    public static final String cashRefundFeeKey = "cash_refund_fee";
    public static final String couponRefundFeePrefixKey = "coupon_refund_fee_";
    public static final String couponRefundFeeKey = "coupon_refund_fee";
    public static final String couponRefundCountPrefixKey = "coupon_refund_count_";
    public static final String couponRefundBatchIdPrefixKey_ = "coupon_refund_batch_id_";
    public static final String couponRefundIdPrefixKey = "coupon_refund_id_";
    public static final String singleCouponRefundFeePrefixKey = "coupon_refund_fee_";

    //查询退款
    public static final String refundStatusPrefixKey = "refund_status_";
    public static final String refundStatusKey = "refund_status";
    public static final String refundRecvAccoutPrefixKey = "refund_recv_accout_";
    public static final String refundSuccessTimePrefixKey = "refund_success_time_";

    //下载对账单
    public static final String billDateKey = "bill_date";
    public static final String billTypeKey = "bill_type";

    //交易保障
    public static final String interfaceUrlKey = "interface_url";
    public static final String executeTimeKey = "execute_time";
    public static final String userIpKey = "user_ip";
    public static final String timeKey = "time";

    //JS端发起微信支付请求需要的参数
    //https://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E5.BE.AE.E4.BF.A1.E6.94.AF.E4.BB.98
    public static final String prepayAppIdKey = "appId";
    public static final String prepayTimestampKey = "timeStamp";
    public static final String prepayNonceStrKey = "nonceStr";
    public static final String prepayPackageKey = "package";
    public static final String prepaySignTypeKey = "signType";
    public static final String prepaySignKey = "paySign";

    //App端发起微信支付请求需要的参数
    //https://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html#.E5.BE.AE.E4.BF.A1.E6.94.AF.E4.BB.98
    public static final String appAppIdKey = "appid";
    public static final String appPartnerIdKey = "partnerid";
    public static final String appPrepayIdKey = "prepayid";
    public static final String appPackageKey = "package";
    public static final String appNonceStrKey = "noncestr";
    public static final String appTimestampKey = "timestamp";
    public static final String appSignKey = "sign";
}
