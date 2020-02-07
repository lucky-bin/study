# JVM内存调优

## 内存预热

1. 如果程序在刚开始的时候就需要很大的内存，我们可以使用  **-XX:AlwaysPreTouch** 参数来告诉jvm，不需要预热，直接使用设定的**最小堆内存(Xms)**

2. 如果短时间内使用了大量的内存，**会造成频繁的FullGC**，可以采用这种方式

   1. 以server模式运行并开启gc输出  -server -verbose:gc -XX:+PrintGCDetails, 分析是不是手动调用System.gc()方法
   2. 如果不是则需要去分析快照文件去具体分析，手动刷快照文件的方式

   ```
   1. jcmd pid GC.heap_dump 路径/文件名
   2. jmap -dump:format=b,file=路径/文件名 pid
   ```

   ​		推荐开启自动dump：

   ```
   -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=路径/文件名
   ```

   3. 分析Dump

      1. windows 下 使用 jvisualvm 或者 Eclipse 的 mat工具分析出来原因

      2. windows 下 使用jvisualvm中的插件Btrace插件写入如下模板即可

         ```java
         /* BTrace Script Template */
         import com.sun.btrace.annotations.*;
         import static com.sun.btrace.BTraceUtils.*;
         
         @BTrace
         public class TracingScript {
         	/* put your code here */
             //clazz 检测的类  method 检测的方法
             @OnMethod(clazz = "java.nio.ByteBuffer", method = "allocateDirect")
             public static void traceExecute(){
              	println("是谁在调用");
             	// 输出调用的堆栈信息
                 jstack();   
             }
         }
         ```

      3.  linux下使用Btrace

         ```shell
         wget https://github.com/btraceio/btrace/releases/download/v1.3.11.3/btrace-bin-1.3.11.3.tgz
         
         # 进入btrace根目录
         bin/btrace -cp build/ pid TracingScript.java
         ```