package com.example.study.thread;

public class ThreadLocalDemo {

    /** threadLocal变量，每个线程都有一个副本，互不干扰 */

    public static ThreadLocal<String> value = new ThreadLocal<String>();

    public static void main(String[] args) throws Exception {

        new Thread(() -> {
                value.set("111111111");

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(value.get());
                value.remove();
            }).start();

        new Thread(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(value.get());
                value.remove();
            }).start();
    }
}
