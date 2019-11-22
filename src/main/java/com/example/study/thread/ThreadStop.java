package com.example.study.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadStop {

    public static void main(String[] args) throws InterruptedException {

        StopThread thread = new StopThread();
        thread.start();
        // 休眠1秒，确保i变量自增成功
        Thread.sleep(1000);
        // 中止线程  清楚中断状态，抛出InterruptedException异常
        thread.interrupt();
        // 终止线程  会出现 i != j的问题
//        thread.stop();

        // 确保线程已经终止
        while (thread.isAlive()) {
        }
        // 输出结果
        thread.print();
    }

    public static class StopThread extends Thread {

        private int i = 0, j = 0;

        @Override
        public void run() {
            synchronized (this) {
                ++i;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ++j;
            }
            System.out.println("锁释放。。。");
        }

        public void print() {
            System.out.println("i=" + i + " j=" + j);
        }
    }
}
