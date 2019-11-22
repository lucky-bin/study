package com.example.study.excutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest {

    static ArrayList<String> urls = new ArrayList<String>() {
        {
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
        }
    };

    // 本质是一个线程池,默认的线程数量:CPU的核数
    static ForkJoinPool forkJoinPool = new ForkJoinPool(3,
            ForkJoinPool.defaultForkJoinWorkerThreadFactory,
            null,
            true);


    public static void main(String args[]) throws ExecutionException, InterruptedException {
        Job job = new Job(urls, 0, urls.size());
        ForkJoinTask<String> forkjoinTask = forkJoinPool.submit(job);


        String result = forkjoinTask.get();
        System.out.println(result);
    }

    public static String doRequest(String url, int index) {
        //模拟网络请求
        return (index + "Kody ... test ... " + url + "\n");
    }


    static class Job extends RecursiveTask<String> {

        List<String> urls;
        int start;
        int end;

        public Job(List<String> urls, int start, int end) {
            this.urls = urls;
            this.start = start;
            this.end = end;
        }


        @Override
        protected String compute() {
            int count = end - start;         //计算这个任务有多大

            //什么时候对任务进行拆分
            if (count <= 10) {
                //直接执行
                String rsult = "";
                for (int i = start; i < end; i++) {
                    String response = doRequest(urls.get(i), i);
                    rsult += response;
                }
                return rsult;
            } else {
                //拆分任务
                int x = (start + end) / 2;

                Job job1 = new Job(urls, start, x);
                job1.fork();
                Job job2 = new Job(urls, x, end);
                job2.fork();

                //固定写法
                String result = "";
                result += job1.join();
                result += job2.join();
                return result;
            }

        }
    }
}



