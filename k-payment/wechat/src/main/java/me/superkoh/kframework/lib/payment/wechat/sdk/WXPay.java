package me.superkoh.kframework.lib.payment.wechat.sdk;

import me.superkoh.kframework.lib.payment.wechat.sdk.business.*;
import me.superkoh.kframework.lib.payment.wechat.sdk.common.Configure;
import me.superkoh.kframework.lib.payment.wechat.sdk.common.Signature;
import me.superkoh.kframework.lib.payment.wechat.sdk.common.WXPerAppConfig;
import me.superkoh.kframework.lib.payment.wechat.sdk.protocol.WXPayData;
import me.superkoh.kframework.lib.payment.wechat.sdk.service.*;

/**
 * SDK总入口
 */
public class WXPay {

    /**
     * 初始化SDK依赖的几个关键配置
     */
    public static void initSDKConfiguration(String ip){
        Configure.setIp(ip);
    }

    /**
     * 请求支付服务
     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的数据
     * @throws Exception
     */
    public static String requestScanPayService(WXPayData scanPayReqData) throws Exception{
        return new ScanPayService().request(scanPayReqData);
    }

    /**
     * 统一下单服务
     * @param orderReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回数据
     * @throws Exception
     */
    public static String requestUnifiedOrderService(WXPayData orderReqData, WXPerAppConfig appConfig) throws Exception {
        return new UnifiedOrderService(appConfig).request(orderReqData);
    }

    /**
     * 请求支付查询服务
     * @param payQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
	public static String requestPayQueryService(WXPayData payQueryReqData, WXPerAppConfig appConfig) throws Exception{
		return new ScanPayQueryService(appConfig).request(payQueryReqData);
	}

    /**
     * 请求订单关闭服务
     * @param closeReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public static String requestCloseService(WXPayData closeReqData, WXPerAppConfig appConfig) throws Exception{
        return new CloseService(appConfig).request(closeReqData);
    }

    /**
     * 请求退款服务
     * @param refundReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public static String requestRefundService(WXPayData refundReqData, WXPerAppConfig appConfig) throws Exception{
        return new RefundService(appConfig).request(refundReqData);
    }

    /**
     * 请求退款查询服务
     * @param refundQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
	public static String requestRefundQueryService(WXPayData refundQueryReqData, WXPerAppConfig appConfig) throws Exception{
		return new RefundQueryService(appConfig).request(refundQueryReqData);
	}

    /**
     * 请求撤销服务
     * @param reverseReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
	public static String requestReverseService(WXPayData reverseReqData) throws Exception{
		return new ReverseService().request(reverseReqData);
	}

    /**
     * 请求对账单下载服务
     * @param downloadBillReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public static String requestDownloadBillService(WXPayData downloadBillReqData) throws Exception{
        return new DownloadBillService().request(downloadBillReqData);
    }

    /**
     * 直接执行被扫支付业务逻辑（包含最佳实践流程）
     * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @throws Exception
     */
    public static void doScanPayBusiness(WXPayData scanPayReqData, ScanPayBusiness.ResultListener resultListener) throws Exception {
        new ScanPayBusiness().run(scanPayReqData, resultListener);
    }

    /**
     * 调用统一下单业务逻辑
     * @param orderReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @throws Exception
     */
    public static void doUnifiedOrderBusiness(WXPayData orderReqData) throws Exception {
        new UnifiedOrderBusiness().run(orderReqData);
    }

    /**
     * 调用退款业务逻辑
     * @param refundReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 业务逻辑可能走到的结果分支，需要商户处理
     * @throws Exception
     */
    public static void doRefundBusiness(WXPayData refundReqData, RefundBusiness.ResultListener resultListener) throws Exception {
        new RefundBusiness().run(refundReqData,resultListener);
    }

    /**
     * 运行退款查询的业务逻辑
     * @param refundQueryReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @throws Exception
     */
    public static void doRefundQueryBusiness(WXPayData refundQueryReqData,RefundQueryBusiness.ResultListener resultListener) throws Exception {
        new RefundQueryBusiness().run(refundQueryReqData,resultListener);
    }

    /**
     * 请求对账单下载服务
     * @param downloadBillReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 商户需要自己监听被扫支付业务逻辑可能触发的各种分支事件，并做好合理的响应处理
     * @return API返回的XML数据
     * @throws Exception
     */
    public static void doDownloadBillBusiness(WXPayData downloadBillReqData,DownloadBillBusiness.ResultListener resultListener) throws Exception {
        new DownloadBillBusiness().run(downloadBillReqData,resultListener);
    }

    public static WXPayData verifyResultNotify(String notifyStr, String appKey) throws Exception {
        WXPayData res = new WXPayData();
        try {
            if (!Signature.checkIsSignValidFromResponseString(notifyStr, appKey)) {
                res.setValue("return_code", "FAIL");
                res.setValue("return_msg", "微信支付结果通知签名错误");
                return res;
            }
        } catch (Exception e) {
            res.setValue("return_code", "FAIL");
            res.setValue("return_msg", e.getMessage());
            return res;
        }

        return res;
    }
}
