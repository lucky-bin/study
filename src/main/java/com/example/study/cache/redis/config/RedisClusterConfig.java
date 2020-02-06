package com.example.study.cache.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.Arrays;

@Configuration
@Profile("cluster")
public class RedisClusterConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        System.out.println("加载cluster环境下的redis client配置");
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(Arrays.asList(
                "192.168.204.129:7000",
                "192.168.204.129:8000",
                "192.168.204.131:7001",
                "192.168.204.131:8001",
                "192.168.204.136:7003",
                "192.168.204.136:8003"
        ));
        redisClusterConfiguration.setPassword("wbin");
        // 自适应集群变化
        return new LettuceConnectionFactory(redisClusterConfiguration);
    }
}
