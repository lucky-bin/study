package com.example.study.dubbo.consumer;


import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import com.example.study.dubbo.DemoService;

/**
 * dubbo客户端，api配置dubbo方式示例。
 * ApiConsumerConfiguration
 */
public class ApiConsumerConfiguration {

	public static void main(String[] args) {
		// 当前应用配置
		ApplicationConfig application = new ApplicationConfig();
		application.setName("consumer-of-helloworld-app");

		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig("127.0.0.1:2181", "zookeeper");
		// 设置临时节点
		registry.setDynamic(Boolean.FALSE);

		// 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
		// 引用远程服务
		// 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
		ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>();
		reference.setApplication(application);
		reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
		reference.setInterface(DemoService.class);
		reference.setVersion("1.0.0");

		// 和本地bean一样使用demoService
		DemoService demoService = reference.get(); // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
		String hello = demoService.sayHello("API demo");
		System.out.println(hello);
	}

}
