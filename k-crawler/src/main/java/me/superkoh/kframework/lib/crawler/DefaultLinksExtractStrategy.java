package me.superkoh.kframework.lib.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KOH on 2017/5/24.
 * <p>
 * k-framework
 */
public class DefaultLinksExtractStrategy implements LinksExtractStrategy {
    @Override
    public List<URL> getLinks(URL url, String html) {
        Document doc = Jsoup.parse(html);
        List<URL> links = new ArrayList<>();
        doc.getElementsByTag("a").forEach(a -> {
            if (a.hasAttr("href")) {
                String href = a.attr("href");
                if (href.startsWith("javascript:") || href.startsWith("#")) {
                    return;
                }
                if (href.startsWith("http://") || href.startsWith("https://")) {
                    try {
                        links.add(new URL(href));
                    } catch (MalformedURLException e) {
                    }
                } else if (href.startsWith("//")) {
                    try {
                        links.add(new URL(url.getProtocol() + ":" + href));
                    } catch (MalformedURLException e) {
                    }
                } else {
                    if (!href.startsWith("/")) {
                        href = "/" + href;
                    }
                    try {
                        links.add(new URL(url.getProtocol(), url.getHost(), url.getPort(), href));
                    } catch (MalformedURLException ignored) {
                    }
                }
            }
        });
        return links;
    }
}
