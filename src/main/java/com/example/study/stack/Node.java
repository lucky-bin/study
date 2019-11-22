package com.example.study.stack;

// 存储在栈里面元素 -- 对象
public class Node {
    public final String value;
    public Node next;

    public Node(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "value=" + value;
    }
}