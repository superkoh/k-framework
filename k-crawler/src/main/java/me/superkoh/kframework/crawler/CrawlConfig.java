package me.superkoh.kframework.crawler;

import java.nio.charset.Charset;

/**
 * Created by KOH on 2017/5/24.
 * <p>
 * k-framework
 */
public class CrawlConfig {
    private int maxClientNumber = 1;
    private int minClientNumber = 1;
    private String downloadPath;
    private int maxDownloadedNumber;
    private Charset charset = Charset.forName("utf8");

    public int getMinClientNumber() {
        return minClientNumber;
    }

    public void setMinClientNumber(int minClientNumber) {
        this.minClientNumber = minClientNumber;
    }

    public int getMaxDownloadedNumber() {
        return maxDownloadedNumber;
    }

    public void setMaxDownloadedNumber(int maxDownloadedNumber) {
        this.maxDownloadedNumber = maxDownloadedNumber;
    }

    public int getMaxClientNumber() {
        return maxClientNumber;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public void setMaxClientNumber(int maxClientNumber) {
        this.maxClientNumber = maxClientNumber;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
