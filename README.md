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