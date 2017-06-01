package me.superkoh.kframework.lib.sns.wechat.app.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.superkoh.kframework.lib.sns.wechat.app.res.WxAppSessionRes;
import me.superkoh.kframework.lib.sns.wechat.app.res.WxAppUserInfoRes;
import me.superkoh.kframework.lib.sns.wechat.common.api.AbstractWxApi;
import me.superkoh.kframework.lib.sns.wechat.common.exception.WxSnsException;
import okhttp3.Request;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

/**
 * Created by KOH on 2017/4/24.
 * <p>
 * webFramework
 */
public class WxAppApi extends AbstractWxApi {

    private static final Logger logger = LoggerFactory.getLogger(WxAppApi.class);

    private static final String JSCODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    public WxAppApi(String appId, String secret) {
        super(appId, secret);
    }

    public WxAppSessionRes jscode2session(String code) throws WxSnsException {
        Request request = new Request.Builder()
                .url(String.format(JSCODE2SESSION_URL, getAppId(), getSecret(), code))
                .get()
                .build();
        return getResponse(request, WxAppSessionRes.class);
    }

    public WxAppUserInfoRes decryptUserInfo(String encryptedData, String iv, String sessionKey) {
        byte[] res = WechatAESUtils.decrypt(Base64.decodeBase64(encryptedData),
                Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
        if (null != res && res.length > 0) {
            try {
                String resStr = new String(res, "UTF8");
                return getObjectMapper()
                        .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
                        .readValue(resStr, WxAppUserInfoRes.class);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }
        return null;
    }

    public static class WechatAESUtils {

        static {
            Security.addProvider(new BouncyCastleProvider());
        }

        public static byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                Key sKeySpec = new SecretKeySpec(keyByte, "AES");
                AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
                params.init(new IvParameterSpec(ivByte));
                cipher.init(Cipher.DECRYPT_MODE, sKeySpec, params);// 初始化
                return cipher.doFinal(content);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            return null;
        }
    }
}
