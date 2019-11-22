package com.example.study.thread.communication;

public class SuspendResume {
    public static Object iceCream = null;

    public static void main(String args[]) throws Exception {
        SuspendResume demo = new SuspendResume();

        //demo.test1_normal();
        demo.test3_OrderDeadLock();
    }

    public void test1_normal() throws Exception {
        //开启一个线程，代表小朋友
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (iceCream == null) {
                    System.out.println("没有冰激凌，小朋友不开心，等待...");
                    Thread.currentThread().suspend();
                }
                System.out.println("小朋友买到冰激凌，开心回家");
            }
        });
        consumerThread.start();

        Thread.sleep(3000L);    // 3秒之后
        iceCream = new Object();      //店员做好了冰激凌

        System.out.println("通知小朋友");
        consumerThread.resume();    //通知小朋友

    }


    /** 死锁的suspend/resume。 suspend并不会像wait一样释放锁，故此容易写出死锁代码 */
    public void test2_SyncDeadLock() throws Exception {
        //开启一个线程，代表小朋友
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (iceCream == null) {
                    System.out.println("没有冰激凌，小朋友不开心，等待...");
                    synchronized (SuspendResume.class){
                        Thread.currentThread().suspend();
                    }
                }
                System.out.println("小朋友买到冰激凌，开心回家");
            }
        });
        consumerThread.start();

        Thread.sleep(3000L);    // 3秒之后
        iceCream = new Object();      //店员做好了冰激凌

        System.out.println("通知小朋友");
        /*synchronized (this){
            consumerThread.resume();    //通知小朋友
        }*/
        System.out.println(SuspendResume.class);
    }

    /** 导致程序永久挂起的suspend/resume */
    public void test3_OrderDeadLock() throws Exception {
        //开启一个线程，代表小朋友
        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (iceCream == null) {
                    System.out.println("没有冰激凌，小朋友不开心，等待...");

                    try {
                        Thread.sleep(6000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Thread.currentThread().suspend();  //挂起
                }
                System.out.println("小朋友买到冰激凌，开心回家");
            }
        });
        consumerThread.start();

        Thread.sleep(3000L);    // 3秒之后
        iceCream = new Object();      //店员做好了冰激凌

        System.out.println("通知小朋友");
        consumerThread.resume();    //通知小朋友

    }


}
