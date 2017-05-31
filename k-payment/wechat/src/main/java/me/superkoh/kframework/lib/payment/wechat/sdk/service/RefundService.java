package me.superkoh.kframework.lib.payment.wechat.sdk.service;

import me.superkoh.kframework.lib.payment.wechat.sdk.common.Configure;
import me.superkoh.kframework.lib.payment.wechat.sdk.common.WXPerAppConfig;
import me.superkoh.kframework.lib.payment.wechat.sdk.protocol.WXPayData;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 16:04
 */
public class RefundService extends BaseService{

    public RefundService(WXPerAppConfig appConfig) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        super(Configure.REFUND_API, appConfig);
    }

    /**
     * 请求退款服务
     * @param refundReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public String request(WXPayData refundReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = sendPost(refundReqData.toXml());

        return responseString;
    }

}
