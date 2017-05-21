package me.superkoh.kframework.lib.db.mybatis;

import me.superkoh.kframework.lib.db.mybatis.annotation.Mapper;
import me.superkoh.kframework.lib.db.mybatis.interceptor.AuthorTraceInterceptor;
import me.superkoh.kframework.lib.db.mybatis.test.SystemUser;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication(scanBasePackages = {"me.superkoh.kframework"})
@MapperScan(basePackages = {"me.superkoh.kframework"}, annotationClass = Mapper.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
    }


    @Bean
    @Order()
    public AuthorTraceInterceptor<SystemUser> authorTraceInterceptor() {
        return new AuthorTraceInterceptor<>(SystemUser.class);
    }
}
