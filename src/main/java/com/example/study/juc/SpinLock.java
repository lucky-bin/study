package com.example.study.juc;

import java.util.concurrent.atomic.AtomicReference;

/*
   自旋锁有两种表现形式：
     1、AtomicIneger  中的使用场景，自旋是为了修改一个变量，直接用CAS去进行操作


     2、如下形式，通过自旋实现了一把锁
 */

public class SpinLock {
    private AtomicReference<Thread> owner = new AtomicReference<Thread>();
    public void lock() {
        Thread current = Thread.currentThread();
        // 利用CAS
        while (!owner.compareAndSet(null, current)) {
            // DO nothing
        }
    }

    public void unlock() {
        Thread current = Thread.currentThread();
        owner.compareAndSet(current, null);
    }


    public static void main(String args[]){
        SpinLock lock = new SpinLock();
        lock.lock();

        lock.unlock();
    }
}
