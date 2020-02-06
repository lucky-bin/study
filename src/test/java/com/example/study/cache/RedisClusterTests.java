package com.example.study.cache;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("cluster")
public class RedisClusterTests extends CacheApplicationTests {

    @Test
    public void cluster(){
        redisServer.setValue("head", "head");
        redisServer.setValue("a", "1");
        redisServer.setValue("foo", "bar");
    }
}
