package com.example.study.Performance;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

// 测试用例： 同时运行2秒，检查谁的次数最多
public class LongPerformanceTest {


    public static void main(String[] args) throws InterruptedException {
        LongPerformanceTest demo = new LongPerformanceTest();
        long atomicCount = demo.testAtomic();
        long adderCount = demo.testLongAdder();
        long LongAcc = demo.testAccumulator();

        System.out.println("atomicCount:" + atomicCount);
        System.out.println("adderCount :" + adderCount);
        System.out.println("LongAcc    :" + LongAcc);
    }

    // AtomicLong方式
    public long testAtomic() throws InterruptedException {
        AtomicLong acount = new AtomicLong(0L);

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                long starttime = System.currentTimeMillis();
                while (System.currentTimeMillis() - starttime < 2000) { // 运行两秒
                    acount.incrementAndGet();
                }
                long endtime = System.currentTimeMillis();
            }).start();
        }
        Thread.sleep(3000);
        return acount.get();
    }

    // LongAdder 方式
    public long testLongAdder() throws InterruptedException {
        LongAdder lacount = new LongAdder();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                long starttime = System.currentTimeMillis();
                while (System.currentTimeMillis() - starttime < 2000) { // 运行两秒
                    lacount.increment();
                }
                long endtime = System.currentTimeMillis();
            }).start();
        }
        Thread.sleep(3000);
        return lacount.sum();
    }

    public long testAccumulator() throws InterruptedException {
        LongAccumulator accumulator = new LongAccumulator((x,y)-> x + y, 0L);

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                long starttime = System.currentTimeMillis();
                while (System.currentTimeMillis() - starttime < 2000) { // 运行两秒
                    accumulator.accumulate(1);
                }
                long endtime = System.currentTimeMillis();
            }).start();
        }
        Thread.sleep(3000);
        return accumulator.get();
    }

}