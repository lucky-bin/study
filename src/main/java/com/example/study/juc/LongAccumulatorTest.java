package com.example.study.juc;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;

public class LongAccumulatorTest {
    public static void main(String[] args) throws InterruptedException {

        LongAccumulator accumulator1 = new LongAccumulator((x,y)-> x+y, 0L);


        for (int i = 0; i < 3; i++) {
            accumulator1.accumulate(1);
        }

        System.out.println(accumulator1.get());

        LongAccumulator accumulator2 = new LongAccumulator((left, right) -> left < right ? left : right, 0);
        // 1000个线程
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            new Thread(() -> {
                accumulator2.accumulate(finalI); // 此处实际就是执行上面定义的操作
            }).start();
        }

        Thread.sleep(2000L);
        System.out.println(accumulator2.longValue()); // 打印出结果

    }
}
