package com.example.study.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.LockSupport;

public class SemaphoreTest {

    public static void main(String args[]){

        Semaphore sp = new Semaphore(10);

        //这么短的时间，发送1000个请求，并发会很高，数据库会受不了
        for (int i=0; i<1000; i++){
            new Thread(){
                @Override
                public void run() {
                    try {
                        sp.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queryDb("localhost:3306");  //模拟DB查询
                    sp.release();
                }
            }.start();
        }

    }
    //发送一个HTTP请求
    public static void queryDb(String uri)  {
        System.out.println("do query...:" + uri);
        LockSupport.parkNanos(1000 * 1000 * 1000);
    }
}
