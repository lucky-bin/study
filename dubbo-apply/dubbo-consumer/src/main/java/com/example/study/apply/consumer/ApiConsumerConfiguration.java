package com.example.study.apply.consumer;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;

import com.example.study.apply.facade.OrderService;
import com.example.study.apply.model.OrderModel;


/**
 * api方式配置dubbo客户端示例
 * ApiConsumerConfiguration
 */
public class ApiConsumerConfiguration {
	private static Logger logger = LoggerFactory.getLogger(ApiConsumerConfiguration.class);
	public static void main(String[] args) {
		// 当前应用配置
		ApplicationConfig application = new ApplicationConfig();
		application.setName("consumer-of-helloworld-app");
		Map<String, String> appParameters = new HashMap<String, String>();
		appParameters.put("qos.enable", "false");
		application.setParameters(appParameters);

		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig("224.5.6.7:1234", "multicast");
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("unicast", "true");
		//registry.setParameters(parameters);

		// 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
		// 引用远程服务
		// 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
		ReferenceConfig<OrderService> reference = new ReferenceConfig<OrderService>();
		reference.setApplication(application);
		reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
		reference.setInterface(OrderService.class);
		reference.setTimeout(2000);
		reference.setVersion("1.0.0");
		
		ReferenceConfigCache cache = ReferenceConfigCache.getCache();

		// 和本地bean一样使用demoService
		OrderService demoService = cache.get(reference); // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
		OrderModel order = new OrderModel();
		order.setOrderName("充值订单");
		order.setOrderType(1);
		order.setUserId("12306");
		String orderNo = demoService.createOrder(order);
		logger.debug("获得订单编号："+orderNo);
		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
