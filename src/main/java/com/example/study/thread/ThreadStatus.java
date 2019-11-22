package com.example.study.thread;

public class ThreadStatus {

    public static void main(String[] args) throws Exception {
        test1();
        test2();
        test3();
    }

    public static void test1() throws InterruptedException {
        // 第一种状态切换 - 新建 -> 运行 -> 终止
        Thread thread1 = new Thread(
                () -> System.out.println("子线程发来贺电。。。")
        );

        System.out.println("1、调用start方法前，thread1状态：" + thread1.getState().toString());
        Thread.sleep(2000L);
        thread1.start();
        System.out.println("2、调用start方法后，thread1状态：" + thread1.getState().toString());

        Thread.sleep(4000L); // 等待thread1执行结束，再看状态
        System.out.println("3、4s后，thread1状态：" + thread1.getState().toString());
        //thread1.start();     //TODO 注意，线程终止之后，再进行调用，会抛出IllegalThreadStateException异常
    }

    public static void test2() throws InterruptedException {
        System.out.println("############第二种：新建 -> 运行 -> 等待 -> 运行 -> 终止(sleep方式)###########################");
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("3、Sleep结束," + Thread.currentThread().getName() +
                    "当前状态：" + Thread.currentThread().getState());
        });
        thread2.start();
        System.out.println("1、调用start后，thread2状态：" + thread2.getState().toString());

        Thread.sleep(2000L); // 等待200毫秒，再看状态
        System.out.println("2、等待2s后，thread2状态：" + thread2.getState().toString());

    }

    public static void test3() throws InterruptedException {
        System.out.println("############第三种：新建 -> 运行 -> 阻塞 -> 运行 -> 终止###########################");

        //创建thread3，暂不启动
        Thread thread3 = new Thread(() -> {
            System.out.println("2、抢锁之前，thread3状态：" + Thread.currentThread().getState().toString());

            //3、子线程抢锁，阻塞
            synchronized (ThreadStatus.class) {
                System.out.println("5、thread3，拿到锁，继续执行，thread3状态" + Thread.currentThread().getState());
                try {
                    Thread.sleep(100000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        synchronized (ThreadStatus.class) {
            System.out.println("1、主线程拿到锁，启动thread3");
            thread3.start();    //2、启动子线程
            Thread.sleep(5000L);
            System.out.println("3、thread3的状态" + thread3.getState());
            Thread.sleep(5000L);
        }
        System.out.println("4、主线程释放锁");
    }
}
