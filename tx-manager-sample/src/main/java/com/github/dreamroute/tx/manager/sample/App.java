package com.github.dreamroute.tx.manager.sample;

import com.github.dreamroute.sqlprinter.starter.anno.EnableSQLPrinter;
import com.github.dreamroute.tx.manager.starter.config.EnableTxManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author w.dehai.2021/7/20.12:32
 */
@EnableSQLPrinter
@SpringBootApplication
@MapperScan(basePackages = "com.github.dreamroute.tx.manager.sample.mapper")
@EnableTxManager(packages = {"com.github.dreamroute.tx.manager.sample.service.impl"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
