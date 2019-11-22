package com.example.study.model;

public class User {
    public volatile int id;
    public volatile int age;

    public User(int id, int age) {
        this.id = id;
        this.age = age;
    }

    public String toString() {
        return "id：" + id + " " + "age：" + age;
    }
}

