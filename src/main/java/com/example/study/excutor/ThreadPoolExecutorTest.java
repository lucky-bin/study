package com.example.study.excutor;

import java.util.concurrent.*;

public class ThreadPoolExecutorTest {

    public static void main(String args[]) throws Exception {
        ThreadPoolExecutorTest test = new ThreadPoolExecutorTest();
        test.test4();
    }

    /**
     * 1、无界队列，超出核心线程数量的线程存活时间：5秒
     *
     * @throws Exception
     */
    private void test1() throws Exception {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,
                10,
                5,      //超过核心线程数的线程，如果超过5s（keepAliveTime）还没有任务给他执行，这个线程就会被销毁
                TimeUnit.SECONDS,                       //keepAliveTime 的时间单位
                new LinkedBlockingQueue<Runnable>(5)     //传入无界的等待队列
        );
        testCommon(threadPoolExecutor);
        // 预计结果：线程池线程数量为：5,超出数量的任务，其他的进入队列中等待被执行
    }

    /**
     * 2、 线程池信息： 核心线程数量5，最大数量10，队列大小3，
     *                超出核心线程数量的线程存活时间：5秒， 指定拒绝策略的
     * @throws Exception
     */
    private void test2() throws Exception {
        // 默认的策略是抛出RejectedExecutionException异常，java.util.concurrent.ThreadPoolExecutor.AbortPolicy
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,
                10,             //最大线程数 10
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(5),   //等待队列容量为3

                //最多容纳13个任务，超出的会被拒绝执行
                new RejectedExecutionHandler() {    //指定 任务拒绝策略
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.err.println("有任务被拒绝执行了");
                    }
                });
        testCommon(threadPoolExecutor);
        // 预计结果：
        // 1、 5个任务直接分配线程开始执行
        // 2、 3个任务进入等待队列
        // 3、 队列不够用，临时加开5个线程来执行任务(5秒没活干就销毁)
        // 4、 队列和线程池都满了，剩下2个任务，没资源了，被拒绝执行。
        // 5、 任务执行，5秒后，如果无任务可执行，销毁临时创建的5个线程
    }

    /**
     * 3、 线程池信息： 核心线程数量5，最大数量5，无界队列，
     *                超出核心线程数量的线程存活时间：5秒
     * @throws Exception
     */
    private void test3() throws Exception {
        //ThreadPoolExecutor pool =  Executors.newFixedThreadPool(5);   //一样的
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,
                5,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
        testCommon(threadPoolExecutor);
        // 预计结：线程池线程数量为：5，超出数量的任务，其他的进入队列中等待被执行
    }

    public void test4_SynchronousQueue() throws InterruptedException {
        // SynchronousQueue，实际上它不是一个真正的队列，因为它不会为队列中元素维护存储空间。
        // 它维护一组线程，这些线程在等待着把元素加入或移出队列。

        //put() 往queue放进去一个element以后就一直wait直到有其他thread进来把这个element取走。
        //take() 取出并且remove掉queue里的element（认为是在queue里的。。。），取不到东西他会一直等。

        final SynchronousQueue<String> queue = new SynchronousQueue<String>();

        new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("before put...");
                    queue.put("a element");
                    System.out.println("after put...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        Thread.currentThread().sleep(100L);

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);

                    System.out.println("before take...");
                    String takedValue = queue.take();
                    System.out.println(takedValue);
                    System.out.println("after take...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    /**
     * 4、 线程池信息：
     * 核心线程数量0，最大数量Integer.MAX_VALUE，
     * SynchronousQueue队列，超出核心线程数量的线程存活时间：60秒
     *
     * @throws Exception
     */
    private void test4() throws Exception {
        // 和Executors.newCachedThreadPool()一样的
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                0,           //核心线程数0
                Integer.MAX_VALUE,      //最大线程数是无限大
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>()
        );
        testCommon(threadPoolExecutor);
        // 预计结果：
        // 1、 线程池线程数量为：30，超出数量的任务，其他的进入队列中等待被执行
        // 2、 所有任务执行结束，60秒后，如果无任务可执行，所有线程全部被销毁，池的大小恢复为0
        Thread.sleep(60000L);
        System.out.println("60秒后，再看线程池中的数量：" + threadPoolExecutor.getPoolSize());
    }


    /**
     * 测试： 提交15个执行时间需要3秒的任务,看线程池的状况
     *
     * @param threadPoolExecutor 传入不同的线程池，看不同的结果
     * @throws Exception
     */
    public void testCommon(ThreadPoolExecutor threadPoolExecutor) throws Exception {
        // 测试： 提交15个执行时间需要3秒的任务，看超过大小的2个，对应的处理情况
        for (int i = 0; i < 30; i++) {
            final int n = i;
            threadPoolExecutor.submit(new Runnable() {
                public void run() {
                    try {
                        System.out.println("任务" + n +" 开始执行");
                        Thread.sleep(3000L);
                        System.err.println("任务" + n +" 执行结束");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("任务" + i + " 提交成功");
        }

        while(true){
            // 查看线程数量，查看队列等待数量
            Thread.sleep(1000L);
            System.out.println(">>> 线程数量：" + threadPoolExecutor.getPoolSize());
            System.out.println(">>> 队列任务数量：" + threadPoolExecutor.getQueue().size());
        }

    }


}
