package com.example.study.cache.redis.config;

import io.lettuce.core.ReadFrom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@Profile("redis-rw")
public class RedisRWConfig {

    @Value("${redis_host}")
    private String redisHost;
    @Value("${redis_port}")
    private Integer redisPort;
    @Value("${redis_password}")
    private String password;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        System.out.println("使用读写分离版本");
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();

        // 默认slave只能进行读取，不能写入
        // 如果应用程序需要往redis写数据，建议连接master
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        serverConfig.setPassword(password);
        return new LettuceConnectionFactory(serverConfig, clientConfig);
    }
}
