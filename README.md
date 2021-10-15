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
    <version>最新版本</version>
</dependency>
```

#### 配置说明
在启动类上加上注解`@EnableTxManager`即可开启全局事务管理，例如：
```java
@EnableTxManager(
        packages = {"com.github.dreamroute.tx.manager.sample.service.impl"},
        rollbackFor = 默认是RuntimeException.class,
        enableReadOnly = true,
        readOnly = {},
        required = {}
)
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```
`@EnableTxManager`包括的属性如下：
```properties
# 【必填】配置需要被事务拦截的包，数组
packages = {"xxx", "yyy"}
# 【非必填】指定应用中回滚异常，默认是RuntimeException
rollbackFor = xxx
# 【非必填】对于查询操作，是否开启只读事务，默认开启
enableReadOnly = true/false
# 【非必填】只读方法，支持通配符，比如get*, select*，默认值是："list*, get*, find*, page*, count*, query*, select*"，
# 如果配置了readOnly，那么就是默认值与用户配置的值的并集
readOnly = {"xxx", "yyy"}
# 【非必填】普通事务，支持通配符，例如insert*, delete*，默认是除开readOnly之外的所有方法
required = {"xxx", "yyy"}
```
```properties
# 如果同一个表达式，既配置了`readOnly`同时又配置了`required`，那么`required`优先级更高，
# 比如下方配置，那么生效的其实是required，但是实际上一般没人这么做
tx.manager.read-only = getUser
tx.manager.required = getUser
```

#### 最佳实践
只需要配置`packages`即可，其他均使用默认值，在项目中查询方法均使用list*, get*, find*, page*, count*, query*, select*之一
