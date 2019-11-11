package com.example.study.dubbo.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 在springboot中通过注解的方式启动dubbo服务
 * SpringBootDubboApplication
 */
@SpringBootApplication
@EnableDubbo
public class SpringBootDubboApplication {
	
	/**
	 * spring-boot方式启动服务
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringBootDubboApplication.class, args);
	}
}
