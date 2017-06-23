package me.superkoh.kframework.lib.crawler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by KOH on 2017/5/24.
 * <p>
 * k-framework
 */
public class Main {
    private static OkHttpClient myHttpClient = new OkHttpClient();

    public static void main(String[] args) throws MalformedURLException {
        CrawlConfig config = new CrawlConfig();
        config.setDownloadPath("/Users/KOH/Downloads/huiben_jd/");
        config.setMaxClientNumber(20);
        config.setMinClientNumber(2);
        config.setMaxDownloadedNumber(20000);
        config.setCharset(Charset.forName("gbk"));
        CrawlController controller = new CrawlController<>(MyCrawler.class, config, new MyProxyGenerator());
        for (int i = 1; i < 200; i = i + 2) {
            controller.addSeedUrl(new URL("https://search.jd.com/search?keyword=%E7%BB%98%E6%9C%AC&enc=utf-8&page=" + i + "&s=53&click=0"));
        }
        controller.setBeforeRestart(() -> {
            for (int i = 1; i < 200; i = i + 2) {
                try {
                    controller.addSeedUrl(new URL("https://search.jd.com/search?keyword=%E7%BB%98%E6%9C%AC&enc=utf-8&page=" + i + "&s=53&click=0"));
                } catch (MalformedURLException e) {
                }
            }
            return true;
        });
        controller.start();
    }

    public static class MyCrawler extends WebCrawler {

        @Override
        public boolean shouldVisit(URL url) {
            String urlStr = url.toString().toLowerCase();
            return urlStr.contains("item.jd.com") &&
                    !Files.exists(Paths.get("/Users/KOH/Downloads/huiben_jd/" + url.getFile().split("\\.")[0].replace("/", "") + ".html"));
        }

        @Override
        public boolean shouldFollowLinksIn(URL url) {
            String urlStr = url.toString().toLowerCase();
            return urlStr.contains("search.jd.com")
                    && urlStr.contains("search?")
                    && urlStr.contains("keyword=%E7%BB%98%E6%9C%AC".toLowerCase());
        }

        @Override
        public boolean shouldDownload(URL url, String html) {
            return this.shouldVisit(url);
        }

        @Override
        public String getDownloadFileName(URL url, String html) {
            return url.getFile().split("\\.")[0];
        }

        @Override
        public LinksExtractStrategy getLinksExtractStrategy(URL url, String html) {
            return new MyLinksExtractStrategy();
        }

        @Override
        public String getUrlIdentity(URL url) {
            if (url.toString().contains("item.jd.com")) {
                return url.getFile().split("\\.")[0];
            } else {
                return super.getUrlIdentity(url);
            }
        }
    }

    public static class MyProxyGenerator implements ProxyGenerator {

        private List<ProxyBean> proxyBeanList = Collections.emptyList();

        @Override
        public List<ProxyBean> nextProxyList() {
            if (!proxyBeanList.isEmpty()) {
                return proxyBeanList;
            }
            try {
                Request request = new Request.Builder()
                        .url("http://www.xdaili.cn/ipagent/greatRecharge/getGreatIp?spiderId=e2b3f77631d945bd9489089acc2bfe9d&orderno=YZ20175240023JfgbZ1&returnType=2&count=20")
                        .get()
                        .build();
                Response response = myHttpClient.newCall(request).execute();
                String json = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ProxyList pl = objectMapper.readValue(json, ProxyList.class);
                if (null != pl) {
                    if (null != pl.getRows()) {
                        proxyBeanList = pl.getRows().stream()
                                .map(row -> new ProxyBean(row.getIp(), row.getPort()))
                                .collect(Collectors.toList());
                        return proxyBeanList;
                    }
                }
                proxyBeanList = Collections.emptyList();
            } catch (Exception e) {
                e.printStackTrace();
                proxyBeanList = Collections.emptyList();
            }
            return proxyBeanList;
        }
    }

    public static class ProxyList {
        @JsonProperty("RESULT")
        private List<ProxyRow> rows;

        public List<ProxyRow> getRows() {
            return rows;
        }

        public void setRows(List<ProxyRow> rows) {
            this.rows = rows;
        }
    }

    public static class ProxyRow {
        private String ip;
        private Integer port;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }

    public static class MyLinksExtractStrategy implements LinksExtractStrategy {

        @Override
        public List<URL> getLinks(URL url, String html) {
            Document doc = Jsoup.parse(html);
            List<URL> links = new ArrayList<>();
            if (url.toString().toLowerCase().contains("item.jd.com")) {
                return links;
            }
            Element element = doc.getElementById("J_goodsList");
            if (null != element) {
                element.getElementsByTag("li").forEach(li -> {
                    String itemId = li.attr("data-sku");
                    if (itemId.isEmpty()) return;
                    try {
                        links.add(new URL("https://item.jd.com/" + itemId + ".html"));
                    } catch (MalformedURLException e) {
                    }
                    try {
                        Element priceE = li.getElementsByClass("p-price").first()
                                .getElementsByClass("J_" + itemId).first();
                        String price = priceE.attr("data-price");
                        Element commentE = li.getElementById("J_comment_" + itemId);
                        String comment = "0";
                        if (null != commentE) {
                            comment = commentE.text().trim();
                        }
                        StandardOpenOption option = StandardOpenOption.APPEND;
                        if (!Files.exists(Paths.get("/Users/KOH/Downloads/huiben_jd_price.txt"))) {
                            option = StandardOpenOption.CREATE;
                        }
                        Files.write(Paths.get("/Users/KOH/Downloads/huiben_jd_price.txt"), (itemId + ":" + price + "," + comment + "\n").getBytes(Charset.forName("gbk")), option);
                    } catch (Exception e) {
                        return;
                    }
                });
            }
            return links;
        }
    }
}
