package com.example.study.thread.communication;

import java.util.concurrent.locks.LockSupport;

public class ParkUnpark {

    public static void main(String args[]) throws Exception {
        ParkUnpark demo = new ParkUnpark();
//        demo.test1_normal();
        demo.test2_DeadLock();
    }

    public static Object iceCream = null;

    /** 正常的park/unpark */
    public void test1_normal() throws Exception {
        //开启一个线程，代表小朋友
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (iceCream == null) {     // 若没有冰激凌
                    System.out.println("没有冰激凌，小朋友不开心，等待...");
                    LockSupport.park();
                }
                System.out.println("小朋友买到冰激凌，开心回家");
            }
        });
        consumerThread.start();

        Thread.sleep(3000L);    // 3秒之后
        iceCream = new Object();    //店员做了一个冰激凌

        LockSupport.unpark(consumerThread);     //通知小朋友

        System.out.println("通知小朋友");
    }

    /** 死锁的park/unpark */
    public void test2_DeadLock() throws Exception {
        //开启一个线程，代表小朋友
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (iceCream == null) {     // 若没有冰激凌
                    System.out.println("没有冰激凌，小朋友不开心，等待...");

                    synchronized (this) {   // 若拿到锁
                        LockSupport.park();     //执行park
                    }
                }
                System.out.println("小朋友买到冰激凌，开心回家");
            }
        });
        consumerThread.start();

        Thread.sleep(3000L);    // 3秒之后
        iceCream = new Object();    //店员做了一个冰激凌

        synchronized (this) {   // 争取到锁以后，才能恢复consumerThread
            LockSupport.unpark(consumerThread);
        }
        System.out.println("通知小朋友");
    }
}
