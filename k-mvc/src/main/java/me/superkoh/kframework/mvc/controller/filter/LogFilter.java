package me.superkoh.kframework.mvc.controller.filter;

import me.superkoh.kframework.core.utils.ACU;
import me.superkoh.kframework.mvc.controller.bean.RequestAttributes;
import me.superkoh.kframework.mvc.controller.interceptor.LogFilterInterceptor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LogFilter implements Filter {
    private static final Logger accessLogger = LoggerFactory.getLogger("accessLogger");

    private List<LogFilterInterceptor> logFilterInterceptorList;

    public LogFilter(List<LogFilterInterceptor> logFilterInterceptorList) {
        if (null == logFilterInterceptorList) {
            this.logFilterInterceptorList = new ArrayList<>();
        } else {
            this.logFilterInterceptorList = logFilterInterceptorList;
        }
    }

    private static void addLogKV(StringBuilder builder, String key, String value) {
        builder.append("[").append(key).append(":").append(value).append("]");
    }

    private static String parseParams(Map<String, String[]> paramMap) {
        StringBuilder builder = new StringBuilder();
        for (String key : paramMap.keySet()) {
            addLogKV(builder, key, Arrays.toString(paramMap.get(key)));
        }
        return builder.toString();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if (!(request instanceof HttpServletRequest)) {
            return;
        }
        ContentCachingRequestWrapper wrapperRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrapperResponse = new ContentCachingResponseWrapper((HttpServletResponse)
                response);
        String ua = wrapperRequest.getHeader("User-Agent");
        ua = null == ua ? "" : ua;
        if (ua.equals("KeepAliveClient") || HttpMethod.HEAD.matches(wrapperRequest.getMethod())) {
            chain.doFilter(wrapperRequest, wrapperResponse);
            wrapperResponse.copyBodyToResponse();
        } else {
            ApplicationContext context = ACU.ctx();
            logFilterInterceptorList.forEach(LogFilterInterceptor::prepare);
            StringBuilder accessLogBuilder = new StringBuilder();
            long startTime = Instant.now().toEpochMilli();
            addLogKV(accessLogBuilder, "start", String.valueOf(startTime));
            chain.doFilter(wrapperRequest, wrapperResponse);
            addLogKV(accessLogBuilder, "status", String.valueOf(wrapperResponse.getStatusCode()));
            RequestAttributes requestAttributes = (RequestAttributes) context.getBean("requestAttributes");
            if (null != requestAttributes.getDeviceToken()) {
                addLogKV(accessLogBuilder, "vd", (String) requestAttributes.getDeviceToken());
            }
            if (null != requestAttributes.getLoginUser()) {
                addLogKV(accessLogBuilder, "user", String.valueOf(requestAttributes.getLoginUser().getId()));
                addLogKV(accessLogBuilder, "token", requestAttributes.getLoginUser().getToken());
                addLogKV(accessLogBuilder, "tokenExpireTime", requestAttributes.getLoginUser().getTokenExpireTime().toString
                        ());

            }
            addLogKV(accessLogBuilder, "locale", RequestContextUtils.getLocale(wrapperRequest).toLanguageTag());
            String remoteIp = requestAttributes.getRemoteIp();
            addLogKV(accessLogBuilder, "prevIp", wrapperRequest.getRemoteAddr());
            addLogKV(accessLogBuilder, "remoteIp", remoteIp);
            addLogKV(accessLogBuilder, "request", wrapperRequest.getRequestURI());
            addLogKV(accessLogBuilder, "method", wrapperRequest.getMethod());
            addLogKV(accessLogBuilder, "params", parseParams(wrapperRequest.getParameterMap()));
            addLogKV(accessLogBuilder, "ua", wrapperRequest.getHeader("User-Agent"));
            if ("POST".equalsIgnoreCase(wrapperRequest.getMethod())) {
                byte[] content = wrapperRequest.getContentAsByteArray();
                if (content.length < 1) {
                    content = StreamUtils.copyToByteArray(wrapperRequest.getInputStream());
                }
                addLogKV(accessLogBuilder, "body", IOUtils.toString(content, "UTF-8").replaceAll("\\r\\n", "")
                        .replaceAll
                                ("\\n", ""));
            }
            if (null != wrapperResponse.getContentType() && (wrapperResponse.getContentType().equals(MediaType
                    .APPLICATION_JSON_UTF8_VALUE) || wrapperResponse.getContentType().equals(MediaType
                    .APPLICATION_JSON_VALUE))) {
                addLogKV(accessLogBuilder, "response", IOUtils.toString(wrapperResponse.getContentAsByteArray(),
                        "UTF-8"));
            }
            logFilterInterceptorList.forEach(logFilterInterceptor -> addLogKV(accessLogBuilder, logFilterInterceptor
                    .getKey(), logFilterInterceptor.getOutput()));
            long endTime = Instant.now().toEpochMilli();
            addLogKV(accessLogBuilder, "duration", String.valueOf(endTime - startTime) + "ms");
            accessLogger.info(accessLogBuilder.toString());
            wrapperResponse.copyBodyToResponse();
        }
    }

    @Override
    public void destroy() {

    }
}
