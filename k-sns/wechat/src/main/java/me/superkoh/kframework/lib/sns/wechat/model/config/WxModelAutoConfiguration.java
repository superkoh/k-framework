package me.superkoh.kframework.lib.sns.wechat.model.config;

import me.superkoh.kframework.lib.sns.wechat.app.api.WxAppApi;
import me.superkoh.kframework.lib.sns.wechat.model.config.properties.WxAppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
@Configuration
@EnableConfigurationProperties({WxAppProperties.class})
public class WxModelAutoConfiguration {
    @Autowired
    private WxAppProperties wxAppProperties;

    @Bean
    public WxAppApi wxAppApi() {
        return new WxAppApi(wxAppProperties.getAppId(), wxAppProperties.getSecret());
    }
}
