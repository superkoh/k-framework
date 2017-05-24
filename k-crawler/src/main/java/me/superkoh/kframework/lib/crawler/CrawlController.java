package me.superkoh.kframework.lib.crawler;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by KOH on 2017/5/24.
 * <p>
 * k-framework
 */
public class CrawlController<T extends WebCrawler> {
    private static final Logger logger = LoggerFactory.getLogger(CrawlController.class);

    private LinkedBlockingQueue<URL> toFetch = new LinkedBlockingQueue<>();
    private CrawlConfig config;
    private ProxyGenerator proxyGenerator;

    private List<OkHttpClient> httpClientList = new ArrayList<>();
    private Set<String> cache = new ConcurrentSkipListSet<>();
    private ExecutorService executorService;
    private AtomicInteger threadCount;
    private List<Future<Boolean>> futureList;

    private Class<T> clazz;

    private LifeCycleFunc beforeRestart;

    public CrawlController(Class<T> clazz, CrawlConfig config) {
        this.clazz = clazz;
        this.config = config;
    }

    public CrawlController(Class<T> clazz, CrawlConfig config, ProxyGenerator proxyGenerator) {
        this(clazz, config);
        this.proxyGenerator = proxyGenerator;
    }

    public void setBeforeRestart(LifeCycleFunc beforeRestart) {
        this.beforeRestart = beforeRestart;
    }

    public void start() {
        threadCount = new AtomicInteger(1);
        updateClients();
        executorService = Executors.newFixedThreadPool(httpClientList.size());
        futureList = new ArrayList<>();
        httpClientList.forEach(okHttpClient -> {
            WebCrawler crawler = newInstance(okHttpClient);
            if (null != crawler) {
                futureList.add(executorService.submit(crawler));
            }
        });
        futureList.forEach(future->{
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.getMessage(), e);
            }
        });
        if (null != beforeRestart) {
            if(!beforeRestart.execute()) {
                return;
            }
        }
        try {
            final AtomicLong count = new AtomicLong(0);
            Files.newDirectoryStream(Paths.get(config.getDownloadPath())).forEach(path -> {
                if (path.toFile().isFile()) {
                    count.incrementAndGet();
                }
            });
            if (count.get() > config.getMaxDownloadedNumber()) {
                return;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("********** New Round **********");
        start();
    }

    private void updateClients() {
        List<OkHttpClient> tmpHttpClientList = new ArrayList<>();
        List<ProxyBean> proxyBeanList = null == proxyGenerator ? null : proxyGenerator.nextProxyList();
        int index = 0;
        for (int i = 0; i < config.getMaxClientNumber(); i++) {
            if (null != proxyBeanList && index == proxyBeanList.size()) {
                index = 0;
            }
            if (null != proxyBeanList && !proxyBeanList.isEmpty()) {
                ProxyBean proxyBean = proxyBeanList.get(index);
                Proxy proxy = new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(proxyBean.getIp(), proxyBean.getPort()));
                OkHttpClient testClient = new OkHttpClient.Builder()
                        .proxy(proxy)
                        .build();
                Request test = new Request.Builder()
                        .url("https://www.baidu.com")
                        .get()
                        .build();
                try {
                    logger.info("Test Proxy " + proxyBean.getIp() + ":" + proxyBean.getPort());
                    Response res = testClient.newCall(test).execute();
                    ResponseBody body = res.body();
                    if (null != body) {
                        body.close();
                        CookieHandler cookieHandler = new CookieManager(
                                null, CookiePolicy.ACCEPT_ALL);
                        OkHttpClient httpClientToAdd = new OkHttpClient.Builder()
                                .cookieJar(new JavaNetCookieJar(cookieHandler))
                                .proxy(proxy)
                                .build();
                        tmpHttpClientList.add(httpClientToAdd);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                index++;
            }
        }
        if (tmpHttpClientList.isEmpty() && httpClientList.isEmpty()) {
            for (int i = 0; i < config.getMaxClientNumber(); i++) {
                httpClientList.add(new OkHttpClient.Builder().build());
            }
        } else if (!tmpHttpClientList.isEmpty()) {
            httpClientList.clear();
            httpClientList.addAll(tmpHttpClientList);
        }
    }

    private WebCrawler newInstance(OkHttpClient httpClient) {
        try {
            T instance = clazz.newInstance();
            instance.setHttpClient(httpClient);
            instance.setController(this);
            return instance;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void addSeedUrl(URL url) {
        toFetch.add(url);
    }

    public LinkedBlockingQueue<URL> getToFetch() {
        return toFetch;
    }

    public void setToFetch(LinkedBlockingQueue<URL> toFetch) {
        this.toFetch = toFetch;
    }

    public CrawlConfig getConfig() {
        return config;
    }

    public void setConfig(CrawlConfig config) {
        this.config = config;
    }

    public Set<String> getCache() {
        return cache;
    }

    public void setCache(Set<String> cache) {
        this.cache = cache;
    }

    public AtomicInteger getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(AtomicInteger threadCount) {
        this.threadCount = threadCount;
    }
}
