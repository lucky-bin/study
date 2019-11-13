# **Dubbo** 
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

## **Dubbo SPI**


## **Dubbo MOCK**


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
