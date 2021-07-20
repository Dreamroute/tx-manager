package com.github.dreamroute.tx.manager.starter.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author w.dehai.2021/7/20.11:37
 */
@Slf4j
@Aspect
@Configuration
@EnableConfigurationProperties(TxManagerProperties.class)
public class TxManagerAutoConfiguration {

    @Resource
    private TxManagerProperties properties;

    @Resource
    private TransactionManager txManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        // 只读事务
        RuleBasedTransactionAttribute readOnly = new RuleBasedTransactionAttribute();
        readOnly.setReadOnly(true);

        // 普通事务
        RuleBasedTransactionAttribute require = new RuleBasedTransactionAttribute();
        String rollbackFor = properties.getRollbackFor();
        Class<?> exceptionCls = rollbackException(rollbackFor);
        require.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(exceptionCls)));

        Map<String, TransactionAttribute> methods = new HashMap<>();
        methods.put("*", require);

        methods.put("list*", readOnly);
        methods.put("get*", readOnly);
        methods.put("find*", readOnly);
        methods.put("page*", readOnly);
        methods.put("count*", readOnly);
        methods.put("query*", readOnly);
        methods.put("select*", readOnly);

        source.setNameMap(methods);
        return new TransactionInterceptor(txManager, source);
    }

    @Bean(name = "txAdviceAdvisor")
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(properties.getExecutionExpression());
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

    private Class<?> rollbackException(String rollbackFor) {
        Class<?> exceptionCls;
        try {
            exceptionCls = this.getClass().getClassLoader().loadClass(rollbackFor);
            if (!Exception.class.isAssignableFrom(exceptionCls)) {
                throw new IllegalArgumentException("配置tx.manager.rollback-for必须是异常类型");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("配置tx.manager.rollback-for不允许使用: " + rollbackFor);
        }
        return exceptionCls;
    }
}
