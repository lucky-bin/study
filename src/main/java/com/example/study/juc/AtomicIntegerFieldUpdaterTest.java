package com.example.study.juc;

import com.example.study.model.User;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldUpdaterTest {
    // 新建AtomicIntegerFieldUpdater对象，需要指明是哪个类中的哪个字段
    private static AtomicIntegerFieldUpdater<User> atom = AtomicIntegerFieldUpdater.newUpdater(User.class, "id");

    public static void main(String[] args) {
        User user = new User(100, 100);
        atom.addAndGet(user, 50);
        System.out.println("addAndGet(user, 50)             调用后值变为：" + user);
    }
}

