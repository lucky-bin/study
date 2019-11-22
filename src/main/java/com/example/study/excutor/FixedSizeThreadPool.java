package com.example.study.excutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FixedSizeThreadPool {

    //1、需要一个任务仓库
    private BlockingQueue<Runnable> blockingQueue;


    //2、 集合容器，存放工作线程
    private List<Thread> workers;

    //3、普通线程要执行多个task，咱们需要封装一下
    public static class Worker extends Thread {

        private FixedSizeThreadPool pool;

        public Worker(FixedSizeThreadPool pool) {
            this.pool = pool;
        }

        @Override
        public void run() {
            while (this.pool.isWorking || this.pool.blockingQueue.size() > 0) {    //如果isWorking，可以继续循环，如果queue中有任务，也继续循环
                Runnable task = null;

                try {
                    if (this.pool.isWorking)    //若处于正常运行状态
                        task = this.pool.blockingQueue.take();    //如果没有任务，就阻塞等待任务
                    else                                        //若处于shutdown状态
                        task = this.pool.blockingQueue.poll();   //拿任务时不阻塞，因为如果没任务，也不会再有任务过来
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (task != null) {      //当isWorking=false时，任务可能为null
                    task.run();
                }
            }
        }
    }


    //4 初始化线程池
    public FixedSizeThreadPool(int poolSize, int queueSize) {
        if (poolSize <= 0 || queueSize <= 0) {
            throw new IllegalArgumentException("非法参数");
        }
        this.blockingQueue = new LinkedBlockingQueue<Runnable>();

        this.workers = Collections.synchronizedList(new ArrayList<Thread>());

        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker(this);   //实例化Worker对象，实际上是一个Thread对象
            worker.start();         //启动worker对象，实际上就是启动一个线程
            workers.add(worker);    //讲worker加到workers集合中，方便管理
        }
    }

    // 对外提供提交任务的接口 非阻塞
    public boolean submit(Runnable task) {
        if (isWorking) {     //若处于isWorking状态
            return this.blockingQueue.offer(task);  //可以提交任务
        } else {  //否则
            return false;   //直接返回false
        }
    }


    // 对外提供提交任务的接口 阻塞
    public void execute(Runnable task) {
        try {
            if (isWorking)      //若处于isWorking状态
                this.blockingQueue.put(task);   //提交任务
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //关闭线程池
    //a. 禁止往队列提交任务
    //b. 等待仓库中的任务执行
    //c. 关闭的时候，再去那任务就不用阻塞，因为不会有新任务来了
    //d. 关闭的时候，阻塞的线程，就要强行中断
    private volatile boolean isWorking = true;

    public void shutdown() {
        this.isWorking = false;
        for (Thread thread : workers) {       //遍历终止处于阻塞状态的线程
            if (thread.getState().equals(Thread.State.WAITING) ||
                    thread.getState().equals(Thread.State.BLOCKED)) {
                thread.interrupt();
            }
        }
    }
}
