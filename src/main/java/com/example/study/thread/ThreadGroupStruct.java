package com.example.study.thread;

public class ThreadGroupStruct {
    public static void main(String args[]) throws InterruptedException {
        //主线程所属 线程组
        ThreadGroup mainG = Thread.currentThread().getThreadGroup();

        //system 线程组
        System.out.println(mainG.getParent().getName() );

        //打印 主线程所属 线程组
        System.out.println(mainG.getName() );

        //创建出的线程，默认与当前线程同组
        Thread th = new Thread(){
            @Override
            public void run() {
                String groupName = Thread.currentThread().getThreadGroup().getName();
                System.out.println("创建出的线程，默认与当前线程同组" + groupName );
            }
        };

        th.start();


        Thread.sleep(1000L);

        //创建组时，指定父组
        ThreadGroup g2 = new ThreadGroup(mainG.getParent(), "g2");
        System.out.println("g2.parent:" + g2.getParent().getName() );

        //创建出的组，默认与当前线程同组
        ThreadGroup g1 = new ThreadGroup("g1");
        System.out.println("创建出的group，默认与当前线程同组" + g1.getParent().getName() );

    }
}
