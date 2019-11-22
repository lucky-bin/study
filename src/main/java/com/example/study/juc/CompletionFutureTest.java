package com.example.study.juc;

import java.util.concurrent.CompletableFuture;

public class CompletionFutureTest {
    public static void main(String args[]) throws InterruptedException {
        
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        }).thenRun(new Runnable() {
            @Override
            public void run() {
                System.out.println("上一步得到结果。。。" );
            }
        });

        System.out.println("here I am...");
        Thread.sleep(10000L);



        //whenComplete  完成时 触发的事件
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        });

        future1.whenComplete((str, throwable) -> System.out.println(str));



        //complete   手动指定结果
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        });

        Thread.sleep(5000L);
        future2.complete("fsdf");    //手动指定结果

        future2.whenComplete((str, throwable) -> System.out.println(str));



        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "hello")
                .thenCombine(
                        CompletableFuture.supplyAsync(() -> "world"), (s1, s2) -> s1 + " " + s2
                );

        future3.whenComplete((str, throwable) -> System.out.println(str));
    }
}
