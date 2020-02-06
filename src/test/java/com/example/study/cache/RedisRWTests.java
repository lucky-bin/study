package com.example.study.cache;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("redis-rw")
public class RedisRWTests extends CacheApplicationTests {

    @Test
    public void rw() {
        redisServer.setValue("123", "456");
        System.out.println(redisServer.getValue("123"));
    }
}
