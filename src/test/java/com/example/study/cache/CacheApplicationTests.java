package com.example.study.cache;

import com.example.study.cache.redis.server.RedisServer;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheApplicationTests {

    @Autowired
    RedisServer redisServer;
    @Autowired
    Environment environment;
}
