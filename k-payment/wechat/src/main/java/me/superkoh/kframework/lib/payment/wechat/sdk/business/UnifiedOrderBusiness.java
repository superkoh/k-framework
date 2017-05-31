package me.superkoh.kframework.lib.payment.wechat.sdk.business;

import me.superkoh.kframework.lib.payment.wechat.sdk.common.Log;
import me.superkoh.kframework.lib.payment.wechat.sdk.common.Signature;
import me.superkoh.kframework.lib.payment.wechat.sdk.common.WXPayConstants;
import me.superkoh.kframework.lib.payment.wechat.sdk.protocol.WXPayData;
import me.superkoh.kframework.lib.payment.wechat.sdk.service.UnifiedOrderService;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * 统一下单业务逻辑
 */
public class UnifiedOrderBusiness {
    public UnifiedOrderBusiness() throws IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        orderService = new UnifiedOrderService(null);
    }

    //打log用
    private static Log log = new Log(LoggerFactory.getLogger(UnifiedOrderBusiness.class));

    //执行结果
    private static String result = "";

    private UnifiedOrderService orderService;

    /**
     * 调用退款业务逻辑
     * @param orderReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @throws Exception
     */
    public void run(WXPayData orderReqData) throws Exception {

        //--------------------------------------------------------------------
        //构造请求“统一下单API”所需要提交的数据
        //--------------------------------------------------------------------

        //API返回的数据
        String orderServiceResponseString;

        log.i("统一下单API返回的数据如下：");
        orderServiceResponseString = orderService.request(orderReqData);
        log.i(orderServiceResponseString);

        //将从API返回的XML数据映射到Java对象
        WXPayData orderResData = new WXPayData();
        orderResData.fromXml(orderServiceResponseString);

        if (orderResData.getValue(WXPayConstants.returnCodeKey) == null) {
            setResult("Case1:退款API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问",Log.LOG_TYPE_ERROR);
            return;
        }

        //Debug:查看数据是否正常被填充到scanPayResponseData这个对象中
        //Util.reflect(orderResData);

        if (orderResData.getValue(WXPayConstants.returnCodeKey).equals("FAIL")) {
            ///注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
            setResult("Case2:退款API系统返回失败，请检测Post给API的数据是否规范合法",Log.LOG_TYPE_ERROR);
//            resultListener.onFailByReturnCodeFail(orderResData);
        } else {
            log.i("退款API系统成功返回数据");
            //--------------------------------------------------------------------
            //收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
            //--------------------------------------------------------------------

            if (!Signature.checkIsSignValidFromResponseString(orderServiceResponseString, null)) {
                setResult("Case3:退款请求API返回的数据签名验证失败，有可能数据被篡改了",Log.LOG_TYPE_ERROR);
//                resultListener.onFailBySignInvalid(orderResData);
                return;
            }

            if (orderResData.getValue(WXPayConstants.resultCodeKey).equals("FAIL")) {
                log.i("出错，错误码：" + orderResData.getValue(WXPayConstants.errCodeKey) + "     错误信息：" +
                        orderResData.getValue(WXPayConstants.errCodeDesKey));
                setResult("Case4:【退款失败】",Log.LOG_TYPE_ERROR);
                //统一下单失败
//                resultListener.onUnifiedOrderFail(orderResData);
            } else {
                //统一下单成功
                setResult("Case5:【退款成功】",Log.LOG_TYPE_INFO);
//                resultListener.onUnifiedOrderSuccess(orderResData);
            }
        }
    }

    public void setOrderService(UnifiedOrderService orderService) {
        this.orderService = orderService;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        UnifiedOrderBusiness.result = result;
    }

    public void setResult(String result,String type){
        setResult(result);
        log.log(type,result);
    }
}
