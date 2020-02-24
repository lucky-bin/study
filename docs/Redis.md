# redis 相关文档
## 1. redis key的相关淘汰策略

**回收策略配置**
```text
配置文件中设置：maxmemory-policy noeviction
动态调整：config set maxmemory-policy noeviction
```
|回收策略|说明|
|---|---|
|noeviction|客户端尝试执行会让更多内存被使用的命令直接报错|
|allkeys-lru|在所有key里执行LRU算法|
|volatile-lru|在所有已经过期的key里执行LRU算法|
|volatile-lfu|使用过期集在密钥中使用近似LFU进行驱逐|
|allkeys-lfu|使用近似LFU逐出任何键|
|allkeys-random|在所有key里随机回收|
|volatile-random|在已经过期的key里随机回收|
|volatile-ttl|回收已经过期的key，并且优先回收存活时间（TTL）较短的键|

**LRU**
```text
最近最少使用，如果数据最近访问过，那么将来被访问的几率也更高
采用双向链表 + map 来实现
通过对少量keys进行取样，然后回收其中一个最好的key
配置方式： maxmemory-samples 5

代价：访问、删除都需要遍历链表
```

**LFU**
```text
最不经常使用，如果一个数据在最近一段时间内使用次数很少，那么在将来一段时间内被使用的可能性也很小
采用队列的方式实现
如果有新数据过来的时候，通过对数据的访问数量进行排序，然后淘汰引用数最小的
```