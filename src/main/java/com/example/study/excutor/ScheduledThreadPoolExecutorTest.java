package com.example.study.excutor;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class ScheduledThreadPoolExecutorTest {

    static ScheduledThreadPoolExecutor pool =
            new ScheduledThreadPoolExecutor(5);


    public static void main(String[] args) throws Exception {
        test1();
        //test2();
        //test3();
    }


    // 提交一个 一次性任务
    private static void test1() throws Exception {
        //定义一个Runnable对象
        Runnable cmd = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务执行时间：" + System.currentTimeMillis());
            }
        };

        //提交一个一次性任务
        pool.schedule(cmd, 3000, TimeUnit.MILLISECONDS);

        int i = 0;
        while (i < 20) {
            Thread.sleep(1000L);
            i++;
            System.out.println(">>>pool中线程数量：" + pool.getPoolSize());
        }

    }

    //scheduleWithFixedDelay 提交一个重复执行的任务
    public static void test2() {
        Runnable cmd = new Runnable() {
            @Override
            public void run() {
                LockSupport.parkNanos(1000 * 1000 * 1000 * 1L);  //暂停一秒钟，和Thread.sleep(1000L) 效果类似
                System.out.println("任务执行，现在时间：" + System.currentTimeMillis());
            }
        };

        //提交定时任务
        pool.scheduleWithFixedDelay(cmd, 1000, 2000, TimeUnit.MILLISECONDS);
    }

    //scheduleAtFixedRate 提交一个重复执行的任务
    private static void test3() throws Exception {
        Runnable cmd = new Runnable() {
            @Override
            public void run() {
                LockSupport.parkNanos(1000 * 1000 * 1000 * 1L);  //暂停一秒钟，和Thread.sleep(1000L) 效果类似
                System.out.println("任务执行，现在时间：" + System.currentTimeMillis());
            }
        };

        pool.scheduleAtFixedRate(cmd, 1000, 2000, TimeUnit.MILLISECONDS);

    }

}
