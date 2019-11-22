package com.example.study.juc;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

//Count Down Latch 倒计时开关
//CountDownLatch的基本使用
public class CountDownLatchTest {

    static AtomicLong num = new AtomicLong(0);

    public static void main(String args[]) throws InterruptedException {

        System.out.println(UUID.randomUUID().toString());

//        CountDownLatch ctl = new CountDownLatch(11);
//
//        for (int i=0; i< 10; i++){
//            new Thread(){
//                @Override
//                public void run() {
//                    for (int j=0; j< 10000000; j++){
//                        num.getAndIncrement();
//                    }
//                    ctl.countDown();
//                }
//            }.start();
//        }
//
//        ctl.await();     //设置开关，设置门栓
//        System.out.println(num.get());
    }
}
