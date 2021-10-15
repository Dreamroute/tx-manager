package com.github.dreamroute.tx.manager.starter.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author w.dehi.2021-10-14
 */
@Target(TYPE)
@Retention(RUNTIME)
@Import(TxManagerConfig.class)
public @interface EnableTxManager {

    String DEFAULT_READ_ONLY_METHODS = "list*, get*, find*, page*, count*, query*, select*";

    /**
     * 需要被事务管理器拦截的包，一般是service包
     */
    String[] packages();

    /**
     * 回滚策略，默认是抛出{@link RuntimeException}就回滚，你也可以改成{@link Exception}
     */
    Class<?> rollbackFor() default RuntimeException.class;

    /**
     * 是否开启只读事务，默认是关闭，如果不开启，那么对于读操作，将不会开启事务，如果开启，那么对于读操作会施加只读事务
     */
    boolean enableReadOnly() default true;

    /**
     * 只读事务方法，支持通配符，比如get*, select*这种
     */
    String[] readOnly() default {};

    /**
     * 普通事务方法，支持通配符，比如insert*, update*这种，默认除了readOnly之外的所有都是required
     */
    String[] required() default {};
}
