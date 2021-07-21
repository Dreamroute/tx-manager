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
     * 只读事务
     */
    private String readOnly;

    /**
     * 普通事务
     */
    private String required;
}
