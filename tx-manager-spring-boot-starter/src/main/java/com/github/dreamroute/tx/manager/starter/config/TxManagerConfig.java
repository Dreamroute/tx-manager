package com.github.dreamroute.tx.manager.starter.config;

import cn.hutool.core.annotation.AnnotationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
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
public class TxManagerConfig implements ImportBeanDefinitionRegistrar {

    private TransactionManager txManager;

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
        StandardAnnotationMetadata icm = (StandardAnnotationMetadata) importingClassMetadata;
        Class<?> ic = icm.getIntrospectedClass();

        String[] packages = AnnotationUtil.getAnnotationValue(ic, EnableTxManager.class, "packages");
        Class<?> rollbackFor = AnnotationUtil.getAnnotationValue(ic, EnableTxManager.class, "rollbackFor");
        boolean enableReadOnly = AnnotationUtil.getAnnotationValue(ic, EnableTxManager.class, "enableReadOnly");
        String[] readOnly = AnnotationUtil.getAnnotationValue(ic, EnableTxManager.class, "readOnly");
        String[] required = AnnotationUtil.getAnnotationValue(ic, EnableTxManager.class, "required");

        if (registry instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory factory = (DefaultListableBeanFactory) registry;
            Advisor txAdviceAdvisor = txAdviceAdvisor(enableReadOnly, rollbackFor, packages, readOnly, required);
            this.txManager = factory.getBean(TransactionManager.class);
            factory.registerSingleton("txAdviceAdvisor", txAdviceAdvisor);
        }
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