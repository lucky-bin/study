package com.example.study.thread.communication;

public class WaitNotify {


    /*
    * 在本案例中，synchronize不能锁this
    * 这里的Runnable中的this并不是demo指向的对象，而是Runnable的匿名类对象
    * 这会导致调用notify 和调用wait的不是同一个对象
    * */


    public static void main(String args[]) throws Exception {
        WaitNotify demo = new WaitNotify();
        demo.test1_normal();
        //demo.test2_DeadLock();
    }

    public static Object iceCream = null;

    /** 正常的wait/notify */
    public void test1_normal() throws Exception {
        //开启一个线程，代表小朋友
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (iceCream == null) { //若没有冰激凌
                    synchronized (WaitNotify.class) {
                        System.out.println("小朋友拿到锁。。。");
                        try {
                            System.out.println("没有冰激凌，小朋友不开心，等待...");
                            WaitNotify.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("小朋友买到冰激凌，开心回家");
            }
        }).start();

        Thread.sleep(3000L);    // 3秒之后
        iceCream = new Object();      //店员做了一个冰激凌

        synchronized (WaitNotify.class) {
            System.out.println("店员拿到锁。。。");
            WaitNotify.class.notifyAll();
            System.out.println("通知小朋友");
        }
    }

    /** 会导致程序永久等待的wait/notify */
    public void test2_DeadLock() throws Exception {
        //开启一个线程，代表小朋友
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (iceCream == null) {    //若没有冰激凌
                    try {
                        Thread.sleep(5000L);
                        System.out.println("没有冰激凌，小朋友不开心，等待...");
                        synchronized (WaitNotify.class){
                            WaitNotify.class.wait();
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                System.out.println("小朋友买到冰激凌，开心回家");
            }
        }).start();

        Thread.sleep(3000L);        // 3秒之后
        iceCream = new Object();          // 店员做了一个冰激凌

        synchronized (WaitNotify.class){    //通知小朋友
            WaitNotify.class.notifyAll();
        }
        System.out.println("通知小朋友");
    }
}
