package me.superkoh.kframework.crawler;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by KOH on 2017/5/24.
 * <p>
 * k-framework
 */
abstract public class WebCrawler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

    protected CrawlController<?> controller;
    private OkHttpClient httpClient;

    public CrawlController getController() {
        return controller;
    }

    public void setController(CrawlController controller) {
        this.controller = controller;
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    abstract public boolean shouldVisit(URL url);

    abstract public boolean shouldFollowLinksIn(URL url);

    abstract public boolean shouldDownload(URL url, String html);

    abstract public String getDownloadFileName(URL url, String html);

    public String getUrlIdentity(URL url) {
        return url.toString();
    }

    public LinksExtractStrategy getLinksExtractStrategy(URL url, String html) {
        return new DefaultLinksExtractStrategy();
    }

    @Override
    final public void run() {
        controller.getThreadCount().incrementAndGet();
        while (true) {
            if (controller.getToFetch().isEmpty()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            try {
                URL url = (controller.getToFetch()).poll();
                if (!shouldVisit(url) && !shouldFollowLinksIn(url)) {
                    continue;
                }
                if (controller.getCache().contains(getUrlIdentity(url))) {
                    continue;
                }
                Response response;
                try {
                    logger.info("fetch: " + url.toString());
                    response = fetchPage(url.toString());
                } catch (SocketTimeoutException | ConnectException e) {
                    controller.getToFetch().add(url);
                    int num = controller.getThreadCount().decrementAndGet();
                    if (num < controller.getConfig().getMinClientNumber()) {
                        controller.restart();
                    }
                    break;
                }
                if (!response.isSuccessful()) {
                    continue;
                }
                ResponseBody responseBody = response.body();
                if (null == responseBody) {
                    continue;
                }
                String html = responseBody.string();
                if (shouldDownload(url, html)) {
                    save(url, html);
                }
                LinksExtractStrategy strategy = getLinksExtractStrategy(url, html);
                List<URL> links = strategy.getLinks(url, html);
                controller.getToFetch().addAll(links);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Response fetchPage(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .addHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .addHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en;q=0.6")
                .get()
                .build();
        Response response = httpClient.newCall(request).execute();
        return response;
    }

    private void save(URL url, String html) {
        String fileName = getDownloadFileName(url, html);
        Path downloadPath = Paths.get(controller.getConfig().getDownloadPath(), fileName + ".html");
        if (Files.exists(downloadPath)) {
            logger.debug("already downloaded: " + fileName + ".html");
            return;
        }

        try {
            Files.write(downloadPath, html.getBytes(controller.getConfig().getCharset()));
            logger.info("downloaded: " + fileName + ".html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
