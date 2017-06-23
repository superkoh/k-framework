package me.superkoh.kframework.mvc.controller.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

/**
 * Created by KOH on 2017/5/17.
 * <p>
 * kmvc
 */
@Configuration
@ConfigurationProperties("kframework.mvc.response.header")
public class ResponseHeaderProperties {
    private List<String> accessControlAllowOrigins = Arrays.asList("*");
    private List<String> accessControlAllowMethods = Arrays.asList(
            HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name());
    private Long accessControlMaxAge = 1800L;
    private List<String> accessControlAllowHeaders = Arrays.asList(
            "Content-Type", "X-Kmvc-Device-Token", "X-Kmvc-Auth", "X-Kmvc-User-Token", "X-Requested-With");
    private Boolean accessControlAllowCredentials = true;

    public List<String> getAccessControlAllowOrigins() {
        return accessControlAllowOrigins;
    }

    public void setAccessControlAllowOrigins(List<String> accessControlAllowOrigins) {
        this.accessControlAllowOrigins = accessControlAllowOrigins;
    }

    public List<String> getAccessControlAllowMethods() {
        return accessControlAllowMethods;
    }

    public void setAccessControlAllowMethods(List<String> accessControlAllowMethods) {
        this.accessControlAllowMethods = accessControlAllowMethods;
    }

    public Long getAccessControlMaxAge() {
        return accessControlMaxAge;
    }

    public void setAccessControlMaxAge(Long accessControlMaxAge) {
        this.accessControlMaxAge = accessControlMaxAge;
    }

    public List<String> getAccessControlAllowHeaders() {
        return accessControlAllowHeaders;
    }

    public void setAccessControlAllowHeaders(List<String> accessControlAllowHeaders) {
        this.accessControlAllowHeaders = accessControlAllowHeaders;
    }

    public Boolean getAccessControlAllowCredentials() {
        return accessControlAllowCredentials;
    }

    public void setAccessControlAllowCredentials(Boolean accessControlAllowCredentials) {
        this.accessControlAllowCredentials = accessControlAllowCredentials;
    }
}
