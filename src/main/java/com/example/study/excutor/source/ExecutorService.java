package com.example.study.excutor.source;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;


public interface ExecutorService extends Executor {

    //优雅的关闭线程池
    //根据之前提交的任务顺序开始shutdown，但不会接受新的任务，
    // 如果已经shutdown，调用本方法不会有任何影响
    void shutdown();


    /** 立马关闭线程池，粗暴的结束
     *  1、尝试停止所有正在执行的task，
     *  2、不再执行正在等待的task，并且返回一个等待执行的task列表
     *  3、不会再接受新的task

     NOTE：这个方法不会等待正在执行的task结束，用awaitTermination方法可以做到

     除了尝试停止正在执行的任务之外，这个方法没有任何保证*/

    List<Runnable> shutdownNow();

    //返回true，如果当前Executor被shutdown
    boolean isShutdown();

    //如果在shutdown后，所有的task都执行完成，返回true，
    //也就是说，只有执行了shutdown 或 shutdownNow后，该方法才有可能返回true
    boolean isTerminated();

    /*
     * 调用本方法开始阻塞，直到出现如下条件之一：
     * 1、调用shutdown后所有的task都执行完成，
     * 2、或者达到timeout时间，
     * 3、或当前线程中断，
     * */
    boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException;



    //void execute(Runnable command);


    /** 提交一个有返回值的任务，并且返回一个future代表task后面的执行结果，
     *  future的get方法会返回task的执行结果*/
    <T> Future<T> submit(Callable<T> task);


    /**  提交一个任务，任务执行后得到一个future，这个future的get方法会返回null*/
    Future<?> submit(Runnable task);

    /** 提交一个任务，并自己指定返回结果，
     * 任务执行后得到一个future，
     * 这个future的get方法会返回你指定的result*/
    <T> Future<T> submit(Runnable task, T result);



    /** 执行给定的一些task，当所有任务执行完成后，返回一个Future列表，
     * 列表里面有执行的状态和结果*/
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
            throws InterruptedException;

    /** 执行给定的一组tasks，返回一个列表，列表中有他们的状态和结果
     * 当所有任务执行完成，或者到达超时时间，每个future的isDone属性都将是true
     * 没有执行完的任务，就被cancelled*/
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                  long timeout, TimeUnit unit)
            throws InterruptedException;



    /** 执行指定的一组task，返回执行完成的那个任务的结果，如果任何一个任务成功，其他任务就会被cancelled掉
     * 方法执行时，若春如的集合被修改，返回结果将是undefined*/
    <T> T invokeAny(Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException;


    <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                    long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException;
}

