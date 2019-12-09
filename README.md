# IO
[toc]
## 1. IO 基础
### 1.1 BIO
BIO 为同步阻塞IO,在java中表现形式为Socket和ServerSocket  
[BIO服务端代码示例](src/main/java/com/example/study/bio/BIOServer.java)  
[BIO客户端代码示例](src/main/java/com/example/study/bio/BIOClient.java)

### 1.2 NIO(重点)
NIO 为同步非阻塞IO，



在java中主要有三个核心

1. Buffer缓冲区  
    1. 概述
       ```text
       缓冲区本质上是一个可以写入数据的内存块(类似数组),然后可以再次读取.
       此内存块包含在NIO Buffer对象中,该对象提供了一组方法,可以更轻松的使用内存块.
       使用Buffer进行数据写入和读取,需要进行四个步骤:
       1.数据写入缓冲区
       2.调用buffer.flip(),转换为读取模式
       3.缓冲区读取数据
       4.调用buffer.clear()或buffer.compact()转为写模式
       ```
    2. 工作原理
       ```text
       Buffer三个重要属性:
       capacity(容量): 作为一个内存块,buffer具有一定的固定大小
       position(位置): 写入模式是代表写数据的位置.读取模式是代表读取数据的位置
       limit(限制): 写入模式下,limit等于容量. 读取模式下,limit等于写入的数据量
       ```
    3. ByteBuffer内存模型
       ```text
       ByteBuffer为性能关键型代码提供了直接内存(direct堆外)和堆内内存两种实现.
       堆外内存获取的方式: ByteBuffer.allocateDirect(noBytes);
       
       好处：
       1. 进行网络IO或者文件IO时比heapBuffer少一次拷贝(file/socket --- os memory --- jvm heap)
       GC会移动对象内存,在写file或socket的过程中,jvm的视线中会先把数据复制到堆外,在进行写入.
       2. GC范围之外,降低GC压力,但实现了自动管理. DirectByteBuffer中有一个Cleaner对象
       (PhantomReference),Cleaner被GC前会执行clean方法,触发DirectByteBuffer中定义的Deallocator
       ```
2.  Channel 通道
3.  Selector 选择器

