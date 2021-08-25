package com.github.dreamroute.tx.manager.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author w.dehai.2021/7/20.11:42
 */
@Data
@ConfigurationProperties(prefix = "tx.manager")
public class TxManagerProperties {
    /**
     * 拦截方法执行事务表达式，如：execution(* com.github.dreamroute.tx.service.impl..*.*(..))
     */
    private String executionExpression;

    /**
     * 指定回滚异常，默认java.lang.RuntimeException
     */
    private String rollbackFor = "java.lang.RuntimeException";

    /**
     * 是否开启readOnly，默认false，true：对于配置了readOnly的方法就会被设置成只读事务，false：对于配置了readOnly的方法就不会被施加事务管理
     */
    private boolean enableReadOnly = false;

    /**
     * 只读事务
     */
    private String readOnly;

    /**
     * 普通事务
     */
    private String required;
}
