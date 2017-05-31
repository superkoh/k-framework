package me.superkoh.kframework.lib.payment.union.service;

import me.superkoh.kframework.lib.payment.common.config.PaymentAccountInfo;
import me.superkoh.kframework.lib.payment.common.service.ThirdPartyPayService;
import me.superkoh.kframework.lib.payment.common.service.info.*;
import me.superkoh.kframework.lib.payment.common.type.PaymentChannel;
import me.superkoh.kframework.lib.payment.common.type.PaymentServiceStatus;
import me.superkoh.kframework.lib.payment.common.type.PaymentStatus;
import me.superkoh.kframework.lib.payment.union.sdk.AcpService;
import me.superkoh.kframework.lib.payment.union.sdk.SDKConfig;
import me.superkoh.kframework.lib.payment.union.sdk.SDKConstants;
import me.superkoh.kframework.lib.payment.union.utils.UnionpayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 银联支付服务
 * Created by zhangyh on 16/9/8.
 */
@Service(value = "Unionpay")
@ConfigurationProperties(prefix = "goodtalk.service.unionpay")
public class UnionpayServiceImpl implements ThirdPartyPayService {
    private String tradePrefix = "";

    private static final Logger logger = LoggerFactory.getLogger("paymentLogger");

    @Override
    public PaymentPrepayInfo getPrepayInfo(ThirdPartyRequestPayInfo requestPayInfo, PaymentAccountInfo accountInfo) {
        SDKConfig config = extractSDKConfig(accountInfo);

        String tradeId = tradePrefix + requestPayInfo.getTradeId();
        String txnTime = UnionpayUtils.getTimeString(requestPayInfo.getTransactionTime());
        String payTimeout = UnionpayUtils.getTimeString(requestPayInfo.getExpireTime());
        String orderTimeout = UnionpayUtils.getTimeString(requestPayInfo.getTransactionTime() + 5 * 60 - 1);
        String orderTimeoutInterval = "299000"; // 5分钟 - 1 秒
        String amountStr = requestPayInfo.getAmount().toString();
        boolean isWap = requestPayInfo.getChannel().equals(PaymentChannel.WAP);
        UnionPrepayInfo unionPrepayInfo = getUnionPrepayInfo(tradeId, txnTime, amountStr, isWap, payTimeout,orderTimeout, orderTimeoutInterval, config);

        PaymentPrepayInfo paymentPrepayInfo = new PaymentPrepayInfo();
        paymentPrepayInfo.setUnionpay(unionPrepayInfo);
        return paymentPrepayInfo;
    }

    @Override
    public PaymentStatusInfo queryPayResult(String tradeId, Long tradeTime, PaymentAccountInfo accountInfo) {
        SDKConfig config = extractSDKConfig(accountInfo);
        tradeId = tradePrefix + tradeId;
        String txnTime = UnionpayUtils.getTimeString(tradeTime);
        Map<String, String> queryResult = queryPayResultByTradeId(tradeId, txnTime, config);
        return parseUnionpayResponse(tradeId, queryResult, config);
    }

    @Override
    public PaymentStatusInfo closeUnfinishedPay(String tradeId, PaymentAccountInfo accountInfo) {
        SDKConfig config = extractSDKConfig(accountInfo);
        tradeId = tradePrefix + tradeId;
        String txnTime = UnionpayUtils.getTimeString(Instant.now().getEpochSecond());
        Map<String, String> queryResult = queryPayResultByTradeId(tradeId, txnTime, config);
        return parseUnionpayResponse(tradeId, queryResult, config);
    }

    @Override
    public PaymentNotifyProcessInfo handleBackNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfo accountInfo) throws Exception {
        SDKConfig config = extractSDKConfig(accountInfo);
        PaymentNotifyProcessInfo notifyStatus = new PaymentNotifyProcessInfo();
        logger.info("收到银联后台通知" + notifyParams.toString());
        Map<String, String> validateData = encodeValuesByEncoding(encoding, notifyParams);

        //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
        if (!AcpService.validate(validateData, encoding, config)) {
            notifyStatus.getStatusInfo().setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
            logger.info("银联后台通知验证签名结果[失败].");
        } else {
            logger.info("银联后台通知验证签名结果[成功].");

            String orderId = validateData.get("orderId");
            String merId = validateData.get("merId");
            if (!accountInfo.getUnionMerId().equals(merId)) {
                notifyStatus.getStatusInfo().setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
                logger.info("银联后台通知失败，商户ID不匹配: " + merId);
            } else {
                notifyStatus.getStatusInfo().setServiceStatus(PaymentServiceStatus.SUCCESS);
                notifyStatus.getStatusInfo().setStatus(PaymentStatus.SUCCESS);
                notifyStatus.getStatusInfo().setTradeIdWithPrefix(orderId, tradePrefix);
                notifyStatus.getStatusInfo().setTotalAmount(validateData.get("txnAmt"));
                notifyStatus.getStatusInfo().setPaymentTime(Instant.now().getEpochSecond());

                logger.info("银联后台通知成功 订单号: " + orderId);
            }
        }

        return notifyStatus;
    }

    @Override
    public PaymentNotifyProcessInfo handleFrontNotify(String encoding, Map<String, String> notifyParams, PaymentAccountInfo accountInfo) throws Exception {
        SDKConfig config = extractSDKConfig(accountInfo);
        handlePayResultFrontNotify(encoding, notifyParams, config);
        return new PaymentNotifyProcessInfo();
    }

    @Override
    public PaymentNotifyProcessInfo getFailedNotifyStatus(String message) throws Exception {
        return new PaymentNotifyProcessInfo();
    }

    @Override
    public PaymentStatusInfo queryRefundState(String tradeId, PaymentAccountInfo accountInfo) throws Exception {
        return null;
    }

    @Override
    public PaymentStatusInfo applyRefund(String tradeId, int totalFee, PaymentAccountInfo accountInfo) throws Exception {
        return null;
    }

    private UnionPrepayInfo getUnionPrepayInfo(String orderId, String txnTime, String amountStr, boolean isWap, String payTimeout, String orderTimeout, String orderTimeoutInterval, SDKConfig config) {
        Map<String, String> requestData = new HashMap<>();
        /*银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        requestData.put(SDKConstants.param_version, UnionpayUtils.version);             //版本号，全渠道默认值
        requestData.put(SDKConstants.param_encoding, UnionpayUtils.encoding);           //字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put(SDKConstants.param_signMethod, "01");                           //签名方法，只支持 01：RSA方式证书加密
        requestData.put(SDKConstants.param_txnType, "01");                              //交易类型 ，01：消费
        requestData.put(SDKConstants.param_txnSubType, "01");                           //交易子类型， 01：自助消费
        requestData.put(SDKConstants.param_bizType, "000201");                          //业务类型，B2C网关支付，手机wap支付
        requestData.put(SDKConstants.param_channelType, isWap ? "08" : "07");          //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机

        /*商户接入参数***/
        requestData.put(SDKConstants.param_merId, config.getMerchId());         //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
        requestData.put(SDKConstants.param_accessType, "0");                            //接入类型，0：直连商户
        requestData.put(SDKConstants.param_orderId, orderId);                           //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        requestData.put(SDKConstants.param_txnTime, txnTime);                           //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        requestData.put(SDKConstants.param_currencyCode, "156");                        //交易币种（境内商户一般是156 人民币）
        requestData.put(SDKConstants.param_txnAmt, amountStr);                          //交易金额，单位分，不要带小数点
        requestData.put(SDKConstants.param_payTimeout, payTimeout);                     //支付超时时间，超时不能支付
        requestData.put(SDKConstants.param_orderTimeout, orderTimeoutInterval);         //订单接收超时时间, 超时不接收订单
        requestData.put(SDKConstants.param_orderTimeoutInterval, orderTimeoutInterval); //订单接收超时时间, 超时不接收订单

        //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
        //如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
        //异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        requestData.put(SDKConstants.param_frontUrl, config.getFrontUrl());

        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        requestData.put(SDKConstants.param_backUrl, config.getBackUrl());

        /*请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        Map<String, String> submitFromData = AcpService.sign(requestData, UnionpayUtils.encoding, config);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。

        String requestFrontUrl = config.getUrlConfig().getFrontRequestUrl();  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
        String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData, UnionpayUtils.encoding);   //生成自动跳转的Html表单
        //logger.info("打印请求HTML，此为请求报文，为联调排查问题的依据："+html);
        //将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过
        return new UnionPrepayInfo(requestFrontUrl, submitFromData, html);
    }

    private Map<String, String> queryPayResultByTradeId(String orderId, String txnTime, SDKConfig config) {
        Map<String, String> data = new HashMap<>();

        /*银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put(SDKConstants.param_version, UnionpayUtils.version);            //版本号
        data.put(SDKConstants.param_encoding, UnionpayUtils.encoding);          //字符集编码 可以使用UTF-8,GBK两种方式
        data.put(SDKConstants.param_signMethod, "01");                          //签名方法 目前只支持01-RSA方式证书加密
        data.put(SDKConstants.param_txnType, "00");                             //交易类型 00-默认
        data.put(SDKConstants.param_txnSubType, "00");                          //交易子类型  默认00
        data.put(SDKConstants.param_bizType, "000201");                         //业务类型 B2C网关支付，手机wap支付

        /*商户接入参数***/
        data.put(SDKConstants.param_merId, config.getMerchId());                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put(SDKConstants.param_accessType, "0");                           //接入类型，商户接入固定填0，不需修改

        /*要调通交易以下字段必须修改*/
        data.put(SDKConstants.param_orderId, orderId);                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
        data.put(SDKConstants.param_txnTime, txnTime);                 //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

        /*请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->*/
        Map<String, String> reqData = AcpService.sign(data, UnionpayUtils.encoding, config);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。

        String url = config.getUrlConfig().getSingleQueryUrl(); // 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
        //这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        Map<String, String> rspData = AcpService.post(reqData, url, UnionpayUtils.encoding);

        String reqMessage = UnionpayUtils.genHtmlResult(reqData);
        String rspMessage = UnionpayUtils.genHtmlResult(rspData);
        logger.info("reqMessage: " + reqMessage + "\nresMessage: " + rspMessage);
        return rspData;
    }

    public void getTransactionRecordsBySettleDate(String settleDate, SDKConfig config) {
        Map<String, String> data = new HashMap<>();

        // 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改
        data.put(SDKConstants.param_version, UnionpayUtils.version);          //版本号 全渠道默认值
        data.put(SDKConstants.param_encoding, UnionpayUtils.encoding);        //字符集编码 可以使用UTF-8,GBK两种方式
        data.put(SDKConstants.param_signMethod, "01");                        //签名方法 目前只支持01-RSA方式证书加密
        data.put(SDKConstants.param_txnType, "76");                           //交易类型 76-对账文件下载
        data.put(SDKConstants.param_txnSubType, "01");                        //交易子类型 01-对账文件下载
        data.put(SDKConstants.param_bizType, "000000");                       //业务类型，固定

        /*商户接入参数*/
        data.put(SDKConstants.param_accessType, "0");                         //接入类型，商户接入填0，不需修改
        data.put(SDKConstants.param_merId, config.getMerchId());              //商户代码，请替换正式商户号测试，如使用的是自助化平台注册的777开头的商户号，该商户号没有权限测文件下载接口的，请使用测试参数里写的文件下载的商户号和日期测。如需777商户号的真实交易的对账文件，请使用自助化平台下载文件。
        data.put(SDKConstants.param_settleDate, settleDate);                  //清算日期，如果使用正式商户号测试则要修改成自己想要获取对账文件的日期， 测试环境如果使用700000000000001商户号则固定填写0119
        data.put(SDKConstants.param_txnTime,UnionpayUtils.getCurrentTime());  //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        data.put(SDKConstants.param_fileType, "00");                          //文件类型，一般商户填写00即可

        /*请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->*/

        Map<String, String> reqData = AcpService.sign(data, UnionpayUtils.encoding, config);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = config.getUrlConfig().getFileTransUrl();//获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.fileTransUrl
        Map<String, String> rspData =  AcpService.post(reqData, url, UnionpayUtils.encoding);

//        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
//        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
//        if(!rspData.isEmpty()){
//            if(AcpService.validate(rspData, UnionpayUtils.encoding)){
//                logger.info("验证签名成功");
//                String respCode = rspData.get("respCode");
//                if("00".equals(respCode)){
//                    //交易成功，解析返回报文中的fileContent并落地
//                    AcpService.deCodeFileContent(rspData, "/Users/zhangyh/workspace/union-pay-cert" ,UnionpayUtils.encoding);
//                }else{
//                    //其他应答码为失败请排查原因
//                }
//            }else{
//                logger.error("验证签名失败");
//            }
//        }else{
//            //未返回正确的http状态
//            logger.error("未获取到返回报文或返回http状态码非200");
//        }

        String reqMessage = UnionpayUtils.genHtmlResult(reqData);
        String rspMessage = UnionpayUtils.genHtmlResult(rspData);
        logger.info("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
    }

    public void revokeByOrderId(String orderId, String queryId, String txnAmt, SDKConfig config) {
        Map<String, String> requestData = new HashMap<>();

        /*银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        requestData.put(SDKConstants.param_version, UnionpayUtils.version);             //版本号，全渠道默认值
        requestData.put(SDKConstants.param_encoding, UnionpayUtils.encoding);           //字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put(SDKConstants.param_signMethod, "01");                           //签名方法，只支持 01：RSA方式证书加密
        requestData.put(SDKConstants.param_txnType, "31");                              //交易类型 ，31：消费撤销
        requestData.put(SDKConstants.param_txnSubType, "00");                           //交易子类型， 默认00
        requestData.put(SDKConstants.param_bizType, "000201");                          //业务类型，B2C网关支付，手机wap支付
        requestData.put(SDKConstants.param_channelType, "07");                          //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机

        /*商户接入参数***/
        requestData.put(SDKConstants.param_merId, config.getMerchId());                 //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
        requestData.put(SDKConstants.param_accessType, "0");                            //接入类型，0：直连商户
        requestData.put(SDKConstants.param_orderId, orderId);                           //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        requestData.put(SDKConstants.param_txnTime, UnionpayUtils.getCurrentTime());    //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        requestData.put(SDKConstants.param_currencyCode, "156");                        //交易币种（境内商户一般是156 人民币）
        requestData.put(SDKConstants.param_txnAmt, txnAmt);                             //交易金额，单位分，不要带小数点
        requestData.put(SDKConstants.param_backUrl, config.getBackUrl());     //后台通知地址

        /*要调通交易以下字段必须修改***/
        requestData.put(SDKConstants.param_origQryId, queryId);                         //【原始交易流水号】，原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取

        /*请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        Map<String, String> submitFormData = AcpService.sign(requestData, UnionpayUtils.encoding, config);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String reqUrl = config.getUrlConfig().getBackRequestUrl();//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl

        Map<String,String> rspData = AcpService.post(submitFormData, reqUrl, UnionpayUtils.encoding);//发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /*对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
//        if(!rspData.isEmpty()){
//            if(AcpService.validate(rspData, UnionpayUtils.encoding)){
//                logger.info("验证签名成功");
//                String respCode = rspData.get("respCode");
//                if("00".equals(respCode)){
//                    //交易已受理(不代表交易已成功），等待接收后台通知确定交易成功，也可以主动发起 查询交易确定交易状态。
//                    System.out.println("respCode = 00");
//                }else if("03".equals(respCode) ||
//                        "04".equals(respCode) ||
//                        "05".equals(respCode)){
//                    //后续需发起交易状态查询交易确定交易状态。
//                }else{
//                    //其他应答码为失败请排查原因
//                }
//            }else{
//                logger.error("验证签名失败");
//            }
//        }else{
//            //未返回正确的http状态
//            logger.error("未获取到返回报文或返回http状态码非200");
//        }
        String reqMessage = UnionpayUtils.genHtmlResult(submitFormData);
        String rspMessage = UnionpayUtils.genHtmlResult(rspData);
        logger.info("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
    }

    public void refundByOrderId(String orderId, String queryId, String txnAmt, SDKConfig config) {
        Map<String, String> requestData = new HashMap<>();

        /*银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        requestData.put(SDKConstants.param_version, UnionpayUtils.version);             //版本号，全渠道默认值
        requestData.put(SDKConstants.param_encoding, UnionpayUtils.encoding);           //字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put(SDKConstants.param_signMethod, "01");                           //签名方法，只支持 01：RSA方式证书加密
        requestData.put(SDKConstants.param_txnType, "04");                              //交易类型 04-退货
        requestData.put(SDKConstants.param_txnSubType, "00");                           //交易子类型， 默认00
        requestData.put(SDKConstants.param_bizType, "000201");                          //业务类型，B2C网关支付，手机wap支付
        requestData.put(SDKConstants.param_channelType, "07");                          //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机

        /*商户接入参数***/
        requestData.put(SDKConstants.param_merId, config.getMerchId());                 //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
        requestData.put(SDKConstants.param_accessType, "0");                            //接入类型，0：直连商户
        requestData.put(SDKConstants.param_orderId, orderId);                           //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        requestData.put(SDKConstants.param_txnTime, UnionpayUtils.getCurrentTime());    //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        requestData.put(SDKConstants.param_currencyCode, "156");                        //交易币种（境内商户一般是156 人民币）
        requestData.put(SDKConstants.param_txnAmt, txnAmt);                             //交易金额，单位分，不要带小数点
        requestData.put(SDKConstants.param_backUrl, config.getBackUrl());     //后台通知地址

        /*要调通交易以下字段必须修改***/
        requestData.put(SDKConstants.param_origQryId, queryId);                         //【原始交易流水号】，原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取

        /*请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面*/
        Map<String, String> submitFormData = AcpService.sign(requestData, UnionpayUtils.encoding, config);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String reqUrl = config.getUrlConfig().getBackRequestUrl();//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl

        Map<String,String> rspData = AcpService.post(submitFormData, reqUrl, UnionpayUtils.encoding);//发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /*对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->*/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
//        if(!rspData.isEmpty()){
//            if(AcpService.validate(rspData, UnionpayUtils.encoding)){
//                logger.info("验证签名成功");
//                String respCode = rspData.get("respCode");
//                if("00".equals(respCode)){
//                    //交易已受理(不代表交易已成功），等待接收后台通知确定交易成功，也可以主动发起 查询交易确定交易状态。
//                    System.out.println("respCode = 00");
//                }else if("03".equals(respCode) ||
//                        "04".equals(respCode) ||
//                        "05".equals(respCode)){
//                    //后续需发起交易状态查询交易确定交易状态。
//                }else{
//                    //其他应答码为失败请排查原因
//                }
//            }else{
//                logger.error("验证签名失败");
//            }
//        }else{
//            //未返回正确的http状态
//            logger.error("未获取到返回报文或返回http状态码非200");
//        }
        String reqMessage = UnionpayUtils.genHtmlResult(submitFormData);
        String rspMessage = UnionpayUtils.genHtmlResult(rspData);
        logger.info("</br>请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
    }

    private void handlePayResultFrontNotify(String encoding, Map<String, String> notifyParams, SDKConfig config) throws Exception {
        logger.info("收到银联前台通知" + notifyParams);
        Map<String, String> validateData = encodeValuesByEncoding(encoding, notifyParams);

        //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
        if (!AcpService.validate(validateData, encoding, config)) {
            logger.info("验证签名结果[失败].");
        } else {
            logger.info("验证签名结果[成功].");
            //【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态

//            String orderId =validateData.get("orderId"); //获取前台通知的数据，其他字段也可用类似方式获取
//            String respCode =validateData.get("respCode"); //获取应答码，收到前台通知了respCode的值一般是00，可以不需要根据这个应答码判断。
        }
    }

    private PaymentStatusInfo parseUnionpayResponse(String tradeId, Map<String, String> queryResult, SDKConfig config) {
        PaymentStatusInfo statusInfo = new PaymentStatusInfo();
        statusInfo.setTradeIdWithPrefix(tradeId, tradePrefix);

        /*对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->*/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        if(!queryResult.isEmpty()) {
            if(AcpService.validate(queryResult, UnionpayUtils.encoding, config)) {
                logger.info("验证签名成功");
                if("00".equals(queryResult.get("respCode"))){//如果查询交易成功
                    //处理被查询交易的应答码逻辑
                    String origRespCode = queryResult.get("origRespCode");
                    if("00".equals(origRespCode)) {
                        //交易成功，更新商户订单状态
                        statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);
                        statusInfo.setStatus(PaymentStatus.SUCCESS);
                        statusInfo.setTradeIdWithPrefix(queryResult.get(SDKConstants.param_orderId), tradePrefix);
                        statusInfo.setTotalAmount(queryResult.get(SDKConstants.param_txnAmt));
                        statusInfo.setPaymentTime(Instant.now().getEpochSecond());
                    } else if ("01".equals(origRespCode)) {
                        //交易失败
                        statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);
                        statusInfo.setStatus(PaymentStatus.PAY_ERROR);
                        statusInfo.setErrorDesc(queryResult.get("origRespMsg"));
                    } else if("02".equals(origRespCode) || "03".equals(origRespCode) ||
                            "04".equals(origRespCode) || "05".equals(origRespCode) ||
                            "06".equals(origRespCode)) {
                        //需再次发起交易状态查询交易
                        statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);
                        statusInfo.setStatus(PaymentStatus.NOT_PAY);
                        statusInfo.setErrorDesc(queryResult.get("origRespMsg"));
                    } else {
                        //其他应答码为失败请排查原因, 下次重试
                        statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);
                        statusInfo.setStatus(PaymentStatus.NOT_PAY);
                        statusInfo.setErrorDesc(queryResult.get("origRespMsg"));
                    }
                } else if ("34".equals(queryResult.get("respCode")) ||
                        "39".equals(queryResult.get("respCode")) ||
                        "10".equals(queryResult.get("respCode"))) {
                    // 查无此交易(34), 交易不在受理时间范围内(39)，参数错误(10)
                    // 如果时间超过过期时间和发起支付时间就算结束。
                    statusInfo.setServiceStatus(PaymentServiceStatus.SUCCESS);
                    statusInfo.setStatus(PaymentStatus.NOT_PAY);
                    statusInfo.setUnionOrderNotExist(true);
                    statusInfo.setErrorDesc(queryResult.get("origRespMsg"));
                } else {
                    //业务调用失败
                    statusInfo.setServiceStatus(PaymentServiceStatus.BUSINESS_ERROR);
                    statusInfo.setServiceErrorCode(queryResult.get("respCode"));
                    statusInfo.setServiceErrorDesc(queryResult.get("respMsg"));
                    logger.error("业务调用失败:" + queryResult.get("respCode") + " 失败描述：" + queryResult.get("respMsg"));
                }
            } else {
                statusInfo.setServiceStatus(PaymentServiceStatus.SIGN_ERROR);
                statusInfo.setServiceErrorDesc("验证签名失败");
                logger.error("验证签名失败");
            }
        } else {
            statusInfo.setServiceStatus(PaymentServiceStatus.NETWORK_ERROR);
            statusInfo.setServiceErrorDesc("网络请求失败");
            logger.error("未获取到返回报文或返回http状态码非200");
        }
        return statusInfo;
    }

    private Map<String, String> encodeValuesByEncoding(String encoding, Map<String, String> notifyParams)
            throws UnsupportedEncodingException {
        Map<String, String> validateData = null;
        if (!notifyParams.isEmpty()) {
            Iterator<Map.Entry<String, String>> it = notifyParams.entrySet().iterator();
            validateData = new HashMap<>(notifyParams.size());
            while (it.hasNext()) {
                Map.Entry<String, String> e = it.next();
                String key = e.getKey();
                String value = e.getValue();
                value = new String(value.getBytes(encoding), encoding);
                validateData.put(key, value);
            }
        }
        return validateData;
    }

    private SDKConfig extractSDKConfig(PaymentAccountInfo accountInfo) {
        SDKConfig config = new SDKConfig();
        config.setMerchId(accountInfo.getUnionMerId());
        config.setBackUrl(accountInfo.getUnionBackUrl());
        config.setFrontUrl(accountInfo.getUnionFrontUrl());
        config.setIsDebugMode(accountInfo.getIsDebugMode());
        config.setSignCertPath(accountInfo.getUnionSignCertPath());
        config.setSignCertPwd(accountInfo.getUnionSignCertPwd());
        config.setSignCertType(accountInfo.getUnionSignCertType());
        config.setEncryptCertPath(accountInfo.getUnionEncryptCertPath());
        config.setValidateCertDir(accountInfo.getUnionValidateCertDir());
        config.setSingleMode(accountInfo.getUnionSingleMode());
        return config;
    }

    public String getTradePrefix() {
        return tradePrefix;
    }

    public void setTradePrefix(String tradePrefix) {
        this.tradePrefix = tradePrefix;
    }
}
