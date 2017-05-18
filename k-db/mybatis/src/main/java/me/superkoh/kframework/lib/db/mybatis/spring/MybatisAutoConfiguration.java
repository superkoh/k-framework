package me.superkoh.kframework.lib.db.mybatis.spring;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by KOH on 2016/12/20.
 */
@Configuration
@ConfigurationProperties("kframework.db.mybatis.datasource")
public class MybatisAutoConfiguration {
    private String url;
    private String username;
    private String password;
    private Integer maxPoolSize = 10;
    private Integer connectionTimeout = 5000;
    private Integer maxLifetime = 1800000;
    private String cachePrepStmts = "true";
    private String prepStmtCacheSize = "250";
    private String prepStmtCacheSqlLimit = "2048";
    private String useServerPrepStmts = "true";

    private String connectionCharacter = "utf8mb4";

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        HikariConfig configuration = new HikariConfig();
        configuration.setJdbcUrl(url);
        configuration.setUsername(username);
        configuration.setPassword(password);
        configuration.setAutoCommit(false);
        configuration.setMaximumPoolSize(maxPoolSize);
        configuration.setConnectionTimeout(connectionTimeout);
        configuration.setMaxLifetime(maxLifetime);
        Properties properties = new Properties();
        properties.setProperty("cachePrepStmts", cachePrepStmts);
        properties.setProperty("prepStmtCacheSize", prepStmtCacheSize);
        properties.setProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
        properties.setProperty("useServerPrepStmts", useServerPrepStmts);
        configuration.setDataSourceProperties(properties);
        configuration.setConnectionInitSql("SET NAMES " + connectionCharacter);
        return new HikariDataSource(configuration);
    }

//    @Bean
//    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
//        AnnotationTransactionAspect.aspectOf().setTransactionManager(transactionManager);
//        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
//        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
//        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        return transactionTemplate;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(Integer maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public String getCachePrepStmts() {
        return cachePrepStmts;
    }

    public void setCachePrepStmts(String cachePrepStmts) {
        this.cachePrepStmts = cachePrepStmts;
    }

    public String getPrepStmtCacheSize() {
        return prepStmtCacheSize;
    }

    public void setPrepStmtCacheSize(String prepStmtCacheSize) {
        this.prepStmtCacheSize = prepStmtCacheSize;
    }

    public String getPrepStmtCacheSqlLimit() {
        return prepStmtCacheSqlLimit;
    }

    public void setPrepStmtCacheSqlLimit(String prepStmtCacheSqlLimit) {
        this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;
    }

    public String getUseServerPrepStmts() {
        return useServerPrepStmts;
    }

    public void setUseServerPrepStmts(String useServerPrepStmts) {
        this.useServerPrepStmts = useServerPrepStmts;
    }

    public String getConnectionCharacter() {
        return connectionCharacter;
    }

    public void setConnectionCharacter(String connectionCharacter) {
        this.connectionCharacter = connectionCharacter;
    }
}
