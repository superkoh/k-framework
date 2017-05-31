package me.superkoh.kframework.lib.payment.union.sdk;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyh on 2017/5/24.
 * 证书预加载，证书获取
 */
public class AcpCertManager {
    private final static Map<String, AcpCert> loadedCert = new HashMap<>();

    public static void loadCertByConfig(SDKConfig config) {
        if (config != null && config.getMerchId() != null && !config.getMerchId().isEmpty()) {
            AcpCert cert = new AcpCert(config);
            loadedCert.put(config.getMerchId(), cert);
        }
    }

    public static AcpCert getCertByConfig(SDKConfig config) {
        if (config != null && config.getMerchId() != null && !config.getMerchId().isEmpty()) {
            return loadedCert.computeIfAbsent(config.getMerchId(), k -> new AcpCert(config));
        }

        return null;
    }
}
