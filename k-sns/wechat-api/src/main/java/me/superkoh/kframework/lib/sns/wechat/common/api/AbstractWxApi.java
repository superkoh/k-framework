package me.superkoh.kframework.lib.sns.wechat.common.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.superkoh.kframework.lib.sns.wechat.common.exception.WxSnsException;
import me.superkoh.kframework.lib.sns.wechat.common.res.WxErrorRes;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by KOH on 2017/6/1.
 * <p>
 * k-framework
 */
abstract public class AbstractWxApi {
    private static final Logger logger = LoggerFactory.getLogger(AbstractWxApi.class);

    private String appId;
    private String secret;
    private OkHttpClient httpClient;
    private ObjectMapper objectMapper;

    public AbstractWxApi(String appId, String secret) {
        this.appId = appId;
        this.secret = secret;
    }

    protected  <T> T getResponse(Request request, Class<T> clazz) throws WxSnsException {
        try {
            Response response = getHttpClient().newCall(request).execute();
            String resStr = response.body().string();
            try {
                WxErrorRes error = getObjectMapper().readValue(resStr, WxErrorRes.class);
                if (null != error.getErrcode()) {
                    throw new WxSnsException(error.getErrcode(), error.getErrmsg());
                }
            } catch (IOException ignored) {
            }
            return getObjectMapper()
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .readValue(resStr, clazz);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new WxSnsException(-1, e.getMessage());
        }
    }

    private OkHttpClient getHttpClient() {
        if (null == httpClient) {
            httpClient = new OkHttpClient();
        }
        return httpClient;
    }

    protected ObjectMapper getObjectMapper() {
        if (null == objectMapper) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
