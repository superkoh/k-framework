package me.superkoh.kframework.lib.db.mybatis;

import me.superkoh.kframework.core.runtime.RuntimeEnv;
import me.superkoh.kframework.lib.db.mybatis.annotation.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"me.superkoh.kframework"})
@MapperScan(basePackages = {"me.superkoh.kframework"}, annotationClass = Mapper.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
    }

    @Bean
    public RuntimeEnv runtimeEnv() {
        return new RuntimeEnv();
    }
}
