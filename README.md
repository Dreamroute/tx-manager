### SpringBoot全局事务配置

#### SpringBoot开起事务的方式：
1. 使用`@Transactional`注解
2. 全局使用xml文件配置，然后使用`@ImportResource(locations = {"classpath:xxx.xml"})方式引入
3. 全局使用Java方式配置，和xml方式类似，只不过换成Java方式了
4. 这里使用的就是Java配置方式

#### 引入依赖
```xml
<dependency>
    <groupId>com.github.dreamroute</groupId>
    <artifactId>tx-manager-spring-boot-starter</artifactId>
    <version>1.0-RELEASE</version>
</dependency>
```

#### 排除依赖
对于不需要事务的微服务，可以使用`@SpringBootApplication(exclude = {TxManagerAutoConfiguration.class})`方式进行排除


#### 配置说明
```properties
# 【必填】配置需要被事务拦截的方法，使用spring的表达式
tx.manager.execution-expression = execution(* com.github.dreamroute.tx.manager.sample.service.impl..*.*(..))
# 【非必填】默认是：java.lang.RuntimeException
tx.manager.rollback-for = java.lang.Exception 
# 【非必填】支持通配符，默认是：list*, get*, find*, page*, count*, query*, select*
tx.manager.read-only = xxx
# 【非必填】支持通配符，默认是：除开read-only之外的方法
tx.manager.required = xxx
```
```properties
# 如果同一个表达式，既配置了`tx.manager.read-only`同时又配置了`tx.manager.required`，那么`required`优先级更高，
# 比如下方配置，那么生效的其实是tx.manager.required
tx.manager.read-only = getUser
tx.manager.required = getUser
```
