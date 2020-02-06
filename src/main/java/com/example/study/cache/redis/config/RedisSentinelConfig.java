package com.example.study.cache.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@Profile("sentinel")
public class RedisSentinelConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        System.out.println("使用哨兵版本");
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                // 集群名称
                .master("mymaster")
                // 哨兵地址
                .sentinel("192.168.204.129", 26379)
                .sentinel("192.168.204.130", 26378)
                .sentinel("192.168.204.136", 26377);
        sentinelConfig.setPassword("wbin");
        return new LettuceConnectionFactory(sentinelConfig);
    }
}
