package com.example.study.juc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class CounterUnsafe {
    int i = 0;

    //Unsafe工具类非常强大，特可以去修改引用类型的值，可以修改对象的属性、可以修改数组 等等
    private static Unsafe unsafe = null;

    //代表了要修改的字段 是一个偏移量
    private static long valueOffset;
    
    static {
        //unsafe = Unsafe.getUnsafe();
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            //指定要修改的字段
            Field iFiled = CounterUnsafe.class.getDeclaredField("i");
            valueOffset = unsafe.objectFieldOffset(iFiled);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void add(){
        //i++;      //普通的java语法实心的操作，没办法保证原子性
        for(;;){
            if (unsafe.compareAndSwapInt(this, valueOffset, i, i+1)){
                return;
            }
        }
    }

}
