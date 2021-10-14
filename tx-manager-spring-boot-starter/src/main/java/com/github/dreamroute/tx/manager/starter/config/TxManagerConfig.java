package com.github.dreamroute.tx.manager.starter.config;

import cn.hutool.core.annotation.AnnotationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.dreamroute.tx.manager.starter.config.EnableTxManager.DEFAULT_READ_ONLY_METHODS;

/**
 * 描述：// TODO
 *
 * @author w.dehi.2021-10-14
 */
@Slf4j
public class TxManagerConfig implements ApplicationContextAware {

    @Resource
    private TransactionManager txManager;
    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public Advisor advisor() {
        Map<String, Object> annotatedBeans = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);
        if (annotatedBeans.isEmpty()) {
            throw new IllegalArgumentException("启动类上需要加上@SpringBootApplication注解");
        }
        Class<?> ic = annotatedBeans.values().toArray()[0].getClass();
        EnableTxManager anno = AnnotationUtils.findAnnotation(ic, EnableTxManager.class);

        String[] packages = AnnotationUtil.getAnnotationValue(ic, EnableTxManager.class, "packages");
        Class<?> rollbackFor = AnnotationUtil.getAnnotationValue(ic, EnableTxManager.class, "rollbackFor");
        boolean enableReadOnly = AnnotationUtil.getAnnotationValue(ic, EnableTxManager.class, "enableReadOnly");
        String[] readOnly = AnnotationUtil.getAnnotationValue(ic, EnableTxManager.class, "readOnly");
        String[] required = AnnotationUtil.getAnnotationValue(ic, EnableTxManager.class, "required");

        return txAdviceAdvisor(enableReadOnly, rollbackFor, packages, readOnly, required);
    }

    public Advisor txAdviceAdvisor(boolean enableReadOnly, Class<?> rollbackFor, String[] packages, String[] readOnly, String[] required) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        String execution = Arrays.stream(packages).map(e -> "execution(* " + e.trim() + "..*.*(..))").collect(Collectors.joining(" || "));
        pointcut.setExpression(execution);
        TransactionInterceptor txAdvice = txAdvice(enableReadOnly, rollbackFor, readOnly, required);
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }

    public TransactionInterceptor txAdvice(boolean enableReadOnly, Class<?> rollbackFor, String[] readOnlyMethods, String[] requiredMethods) {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        // 只读事务
        RuleBasedTransactionAttribute readOnly = null;
        if (enableReadOnly) {
            readOnly = new RuleBasedTransactionAttribute();
            readOnly.setReadOnly(true);
        }

        // 普通事务
        RuleBasedTransactionAttribute required = new RuleBasedTransactionAttribute();
        required.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(rollbackFor)));

        Map<String, TransactionAttribute> methods = processExpress(required, readOnly, readOnlyMethods, requiredMethods);
        source.setNameMap(methods);
        return new TransactionInterceptor(txManager, source);
    }

    private Map<String, TransactionAttribute> processExpress(RuleBasedTransactionAttribute required, RuleBasedTransactionAttribute readOnly, String[] readOnlyMethods, String[] requiredMethods) {
        Map<String, TransactionAttribute> methods = new HashMap<>();
        methods.put("*", required);
        Arrays.stream(DEFAULT_READ_ONLY_METHODS.split(",")).map(String::trim).forEach(e -> methods.put(e, readOnly));

        if (readOnlyMethods != null && readOnlyMethods.length > 0) {
            Arrays.stream(readOnlyMethods).map(String::trim).forEach(r -> methods.put(r, readOnly));
        }
        if (requiredMethods != null && requiredMethods.length > 0) {
            Arrays.stream(requiredMethods).map(String::trim).forEach(r -> methods.put(r, required));
        }
        return methods;
    }

}