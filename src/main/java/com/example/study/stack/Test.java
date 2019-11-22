package com.example.study.stack;

import java.util.concurrent.locks.LockSupport;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Stack stack = new Stack();
        //ConcurrentStack stack = new ConcurrentStack();

        stack.push(new Node("B"));      //B入栈
        stack.push(new Node("A"));      //A入栈

        Thread thread1 = new Thread(() -> {
            Node node = stack.pop(800);
            System.out.println(Thread.currentThread().getName() +" "+ node.toString());

            System.out.println("done...");
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            LockSupport.parkNanos(1000 * 1000 * 300L);

            Node nodeA = stack.pop(0);      //取出A
            System.out.println(Thread.currentThread().getName()  +" "+  nodeA.toString());

            Node nodeB = stack.pop(0);      //取出B，之后B处于游离状态
            System.out.println(Thread.currentThread().getName()  +" "+  nodeB.toString());

            stack.push(new Node("D"));      //D入栈
            stack.push(new Node("C"));      //C入栈
            stack.push(nodeA);                    //A入栈

            System.out.println("done...");
        });
        thread2.start();

        LockSupport.parkNanos(1000 * 1000 * 1000 * 2L);


        System.out.println("开始遍历Stack：");
        Node node = null;
        while ((node = stack.pop(0))!=null){
            System.out.println(node.value);
        }
    }
}
