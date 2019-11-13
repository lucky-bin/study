package com.example.study.apply.hystrix;

import org.apache.dubbo.common.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;;


/**
 * 通过Filter来实现熔断功能
 *
 * @author
 */
@Activate(group = CommonConstants.CONSUMER, before = "future")
public class DubboHystrixFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final int DEFAULT_THREADPOOL_CORE_SIZE = 30;    // 默认的核心线程数大小

    /**
     * 当发起调用的时候，进行熔断控制
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        logger.debug("调用熔断器拦截");
        Setter sett = getSetter(invoker, invocation);
        DubboHystrixCommand hystrixCmd = new DubboHystrixCommand(sett, invoker, invocation);
        Result rst = hystrixCmd.execute();
        logger.debug("拦截调用完毕");
        return rst;
    }


    private Setter getSetter(Invoker<?> invoker, Invocation invocation) {
        String methodName = invocation.getMethodName();
        String interfaceName = invoker.getInterface().getName();
        URL url = invoker.getUrl();
        int coreSize = url == null ? DEFAULT_THREADPOOL_CORE_SIZE
                : url.getParameter("hystrixCoreSize", DEFAULT_THREADPOOL_CORE_SIZE);

        return Setter
                // 必填项。指定命令分组名，主要意义是用于统计
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(interfaceName))
                // 依赖名称（如果是服务调用，这里就写具体的接口名， 如果是自定义的操作，就自己命令），默认是command实现类的类名。 熔断配置就是根据这个名称
                .andCommandKey(HystrixCommandKey.Factory.asKey(methodName))
                // 线程池命名，默认是HystrixCommandGroupKey的名称。 线程池配置就是根据这个名称
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(invoker.getInterface().getSimpleName() + "Thread"))
                // command 熔断相关参数配置
                .andCommandPropertiesDefaults(
                		HystrixCommandProperties.Setter()
                                // 配置隔离方式：默认采用线程池隔离。还有一种信号量隔离方式,
                                //.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                                // 超时时间500毫秒
                                .withExecutionTimeoutInMilliseconds(5000)
								// 信号量隔离的模式下，最大的请求数。和线程池大小的意义一样
								// .withExecutionIsolationSemaphoreMaxConcurrentRequests(2)
								// 熔断时间（熔断开启后，各5秒后进入半开启状态，试探是否恢复正常）
								// .withCircuitBreakerSleepWindowInMilliseconds(5000)
                )
                // 设置线程池参数
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
				// 线程池大小
				.withCoreSize(coreSize));
    }

}
