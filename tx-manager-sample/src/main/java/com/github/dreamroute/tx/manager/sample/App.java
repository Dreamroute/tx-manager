package com.github.dreamroute.tx.manager.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author w.dehai.2021/7/20.12:32
 */
@SpringBootApplication
@MapperScan(basePackages = "com.github.dreamroute.tx.manager.sample.mapper")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}