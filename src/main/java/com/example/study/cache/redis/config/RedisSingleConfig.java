package com.example.study.cache.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@Profile("single")
public class RedisSingleConfig {

    @Value("${redis_host}")
    private String redisHost;
    @Value("${redis_port}")
    private Integer redisPort;
    @Value("${redis_password}")
    private String password;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(){
        System.out.println("使用单机版本");
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}
