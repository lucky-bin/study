package com.example.study.cache;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("sentinel")
public class RedisSentinelTests extends CacheApplicationTests{


    @Test
    public void sentinel() throws InterruptedException {
        // 每个一秒钟，操作一下redis，看看最终效果
        int i = 0;
        while (true) {
            i++;
            redisServer.setValue("test-value", String.valueOf(i));
            System.out.println("修改test-value值为: " + i);
            Thread.sleep(1000L);
        }
    }
}
