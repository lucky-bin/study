package com.example.study.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.LockSupport;

public class CyclicBarrierTest {
    public static void main(String[] args) {

        CyclicBarrier barrier  = new CyclicBarrier(4, ()-> System.out.println(">>> 这是一个栅栏。。。"));

        //传入一个Runnable，打印栅栏

        for (int i=0; i< 100; i++){
            new Thread(){
                @Override
                public void run() {
                    try {
                        //LockSupport.parkNanos(1000 * 1000 * 1000 * 10L);
                        barrier.await();    //
                        System.out.println("上到摩天轮...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            LockSupport.parkNanos(1000 * 1000 * 1000L);
        }
    }

}
