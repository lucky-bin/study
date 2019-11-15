# **Dubbo  [官网文档](http://dubbo.apache.org/zh-cn/docs/user/quick-start.html)** 
[TOC] 
## **协议**
##### 1. header(长度16的字节数组): 请求/响应都包含的数据
````
magic(2B): 魔数
flag(1B): 标识
status(1B): 响应状态
messageId(8B): 消息id
bodyLength(4B): 内容长度
````
##### 2. 请求Body(具体的业务操作内容)
````
rpc版本,服务接口路径,服务版本号,服务方法名,参数描述符,参数值序列化,Dubbo内置参数
````
##### 3. 响应Body
````
响应结果类型,返回值
结果类型: 0>异常信息   1>方法返回值   2>返回值为Null
````
## **服务注册(Register)**
````
服务注册开启临时功能
如果服务注册没有dynamic属性，则需要在ip里面加上?dynamic=false
如果有只需要设置dynamic为false
````
## **负载均衡(LoadBalance)**
|负载方式|说明|通俗说明|
|---|---|---|
|random|随机分配|按照权重设置随机概率|
|random|轮询|按照公约后的权重设置轮询概率|
|leastactive|最少活跃调用数|服务处理性能越高，分配概率大;活跃数相同,权重随机|
|consistenthash|一致性hash|相同参数的请求，总是发送给同一提供者|

## **容错机制(Cluster)**
|集群容错方案|说明|
|---|---|
|FailoverCluster|失败自动切换，自动重试其他服务器(dufault)|
|FailfastCluster|快速失败，立即报错，直发器一次调用|
|FailsafeCluster|失败安全，出现异常直接忽略|
|FailbackCluster|失败自动恢复，记录失败请求，定时重发（5s，不可修改）|
|ForkingCluster|并行调用多个服务器，只要一个成功就返回|
|BoradcastCluster|广播逐个调用所有提供者，任意一个报错，就报错|
|AvailableCluster|遍历所有的实例,遍历到第一个可用的实例，调用该实例,如果没有可用的实例，则抛出异常|
|MergeableCluster|聚合集群，将集群中的调用结果聚合起来返回结果|
|RegistryAwareCluster|先通过"default"key查询来自本地注册的invoker，若没有查到，则返回第一个可用的invoker，若都不可用，抛出异常|

## **Dubbo SPI [SPI文档](http://dubbo.apache.org/zh-cn/docs/dev/impls/protocol.html)**

#### **SPI分析**
**@SPI注解解析器 ExtensionLoader,重要方法 getExtension、createExtension**
```java
public class ExtensionLoader{
    public T getExtension(String name) {
        //.... 省略部分代码
        if ("true".equals(name)) {
            //返回默认扩展对象
            return getDefaultExtension();
        }
        // 根据名称创建扩展对象
        Object instance = holder.get();
        //.... 省略部分代码
        return (T) instance;
    }

    private T createExtension(String name) {
        //根据类的信息获取
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw findException(name);
        }
        try {
            T instance = (T) EXTENSION_INSTANCES.get(clazz);
            if (instance == null) {
                //反射 new 出去一个对象
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            }
            // 通过set方法注入需要的扩展点
            injectExtension(instance);
            Set<Class<?>> wrapperClasses = cachedWrapperClasses;
            if (CollectionUtils.isNotEmpty(wrapperClasses)) {
                //扩展点包装类包装                
                for (Class<?> wrapperClass : wrapperClasses) {
                    instance = injectExtension((T) wrapperClass.getConstructor(type).newInstance(instance));
                }
            }
            return instance;
        } catch (Throwable t) {
            throw new IllegalStateException("Extension instance (name: " + name + ", class: " +
                    type + ") couldn't be instantiated: " + t.getMessage(), t);
        }
    }
}
```
```text
通过class反射类来构造class对象实例,injectExtension方法通过set方法注入扩展类中依赖的其他扩展点.
包装类Wraapper,封装了通用的逻辑,通过有误当前扩展参数构造函数来判断,并注入依赖扩展
```
##### **自适应扩展点**
在运行时根据参数值动态决定采用哪个扩展点实现类
```java
//通过动态代码包装实现获取
ExtensionLoader.getExtenSionLoader(ProxyFactory.class).getAdaptiveExtension();
//根据proxyName获取
ExtensionLoader.getExtenSionLoader(ProxyFactory.class).getExtension(proxyName); 
```
**自适应扩展点应用**
```text
需要进行自适应的类或方法设置 @Adaptive
1. 当@Adaptive 在雷伤，不会生成代理类
2.注解在方法(接口方法)上是，DUbbo则会为改方法生成代理逻辑
```

##### **重点代码理解**
```java
public class ServiceConfig{
    private static final ProxyFactory PROXY_FACTORY = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();
    private void doExportUrlsFor1Protocol(ProtocolConfig protocolConfig, List<URL> registryURLs) {
        Invoker<?> invoker = PROXY_FACTORY.getInvoker(ref, (Class) interfaceClass, registryURL.addParameterAndEncoded(EXPORT_KEY, url.toFullString()));
        DelegateProviderMetaDataInvoker wrapperInvoker = new DelegateProviderMetaDataInvoker(invoker, this);
        Exporter<?> exporter = protocol.export(wrapperInvoker);
    }
}
```
```text
1. ExtensionLoader 根据Class和@SPI注解指定的实现名去获取具体的实现类的代理类(动态生成)
2. 将Invoker转变成DelegateProviderMetaDataInvoker
3. 根据invoker获取到服务的控制接口
```
##### **Invoker和Exporter**
```text
Invoker是实体域,Dubbo的核心模型,其他模型都向它靠拢,或转换成它,它代表一个可执行体.可以将所有需要代理执行的方法,用Invoker进行抽象转换.
Exporter 是一个服务暴露控制接口，可以获得暴露的invocker，注销暴露的invoker
```

## **Dubbo 线程模型**
#####  线程模型
````
1.网络IO线程
    用于接收网络请求
    处理网络请求accpet、connection、read、write相关事件。
    如果事件处理的逻辑能迅速完成，直接在IO线程上处理更快，因为减少了线程池调度。
    如果事件逻辑较慢，IO线程一旦阻塞，就无法在处理新的网络请求
2.业务线程
    用于处理复杂的业务逻辑
    通过IO调用第三方服务获得数据，在进行相关计算，获得业务响应。
    处理业务存在耗时不确定的情况，可以见面对dubbo网络IO线程的占用阻塞。
````

#####  派遣方式
|方式|说明|
|---|---|
|all|所有消息都派发到线程池，包括请求，响应，连接时间，断开事件，心跳等|
|direct|所有消息都不派发到线程池，全部在IO线程上执行|
|message|只有请求响应消息派发到线程池，其他连接断开事件，心跳等小洗，直接在IO线程上执行|
|execution|只有请求消息派发到线程池，不包含响应；响应和其他连接断开事件，心跳跳等消息，直接在IO线程上执行|
|connection|在IO线程上，将连接断开事件放入队列，有序逐个执行，其他消息派发到线程池|

#####  线程池配置
|方式|介绍|说明|
|---|---|---|
|fixed|固定大小线程池|启动时简历线程，不关闭，一直持有|
|cached|缓存线程池|空闲一分钟自动删除，需要时重建|
|limited|可伸缩线程池|池中的线程数量只增长不会收缩<br/>为了避免收缩时突然来了大流量引起的性能问题|
|eager|优先创建worker线程池|在任务数量大于corePoolSize，小于maximumPoolSize时，优先创建worker来处理任务<br/>任务数量大于maximumPoolSize时，将任务放入阻塞队列中。<br/>阻塞队列充满时抛出RejectedExcutionException。<br/>(相比于cached: cached在任务数量超过maximumPoolSize时直接抛出异常而不是将任务放入阻塞队列)|

## **服务治理**
改变运行时服务的行为和选址逻辑,达到限流,权重配置等目的

|版本|功能|
|---|---|
|2.7.0-|针对服务接口级别<br/>条件路由<br/>黑白名单<br/>动态配置<br/>权重调节<br/>负载均衡|
|2.7.0+|针对应用级别<br/>标签路由(新增)<br/>条件路由<br/>黑白名单<br/>动态配置<br/>权重调节<br/>负载均衡<br/>配置管理(新增)<br/>元数据中心(新增)|
````
最大区别
老版本针对服务接口级别进行服务治理
新版本支持针对应用级别 
````
[相关官方文档](http://dubbo.apache.org/zh-cn/docs/admin/serviceGovernance.html)