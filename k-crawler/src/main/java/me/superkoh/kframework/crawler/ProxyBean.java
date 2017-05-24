package me.superkoh.kframework.crawler;

/**
 * Created by KOH on 2017/5/24.
 * <p>
 * k-framework
 */
public class ProxyBean {
    private String ip;
    private Integer port;

    public ProxyBean(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

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
