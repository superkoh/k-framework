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
 * Created by zhangyh on 2016/10/12.
 */
public class CloseService extends BaseService {
    public CloseService(WXPerAppConfig appConfig) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        super(Configure.CLOSE_ORDER_API, appConfig);
    }

    /**
     * 请求关闭订单服务
     * @param closeReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public String request(WXPayData closeReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = sendPost(closeReqData.toXml());

        return responseString;
    }
}
