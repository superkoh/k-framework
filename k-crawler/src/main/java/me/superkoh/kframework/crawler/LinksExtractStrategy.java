package me.superkoh.kframework.crawler;

import java.net.URL;
import java.util.List;

/**
 * Created by KOH on 2017/5/24.
 * <p>
 * k-framework
 */
public interface LinksExtractStrategy {
    List<URL> getLinks(URL url, String html);
}
