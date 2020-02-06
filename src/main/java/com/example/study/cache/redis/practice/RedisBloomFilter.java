package com.example.study.cache.redis.practice;

import io.rebloom.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * RedisBloomFilter 插件客户端代码</br>
 * 
 * 项目参考地址：https://github.com/RedisBloom/JRedisBloom
 */
@Service
@Profile("practice")
public class RedisBloomFilter {
	// redis连接信息
	@Value("${redis_host}")
	private String redisHost;
	@Value("${redis_port}")
	private int redisPort;
	// bloomfilter 客户端
	private Client client;
	
	@PostConstruct
	public void init() {
		// bloomfilter 客户端
		client = new Client(redisHost, redisPort);
	}
	
	/**
	 * 创建一个自定义的过滤器
	 * @param filterName
	 */
	public void createFilter(String filterName) {
		// 创建一个容量10万，误判率0.01%的布隆过滤器
		client.createFilter(filterName, 1000, 0.1);
	}
	
	/**
	 * 添加元素
	 * @param filterName
	 * @param value
	 * @return
	 */
	public boolean addElement(String filterName, String value) {
		return client.add(filterName, value);
	}
	
	/**
	 * 判断元素是否存在
	 * @param filterName
	 * @param value
	 * @return
	 */
	public boolean exists(String filterName, String value) {
		return client.exists(filterName, value);
	}
	
}

