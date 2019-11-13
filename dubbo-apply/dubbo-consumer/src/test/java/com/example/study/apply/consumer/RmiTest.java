package com.example.study.apply.consumer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.study.apply.facade.OrderService;
import com.example.study.apply.model.OrderModel;

/**
 * ConcurrentTest
 * 
 */
public class RmiTest {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void test() {
		// 多线程模拟高并发
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
		context.start();
		final OrderService orderService = (OrderService) context.getBean("orderService"); // 获取远程服务代理
		OrderModel order = new OrderModel();
		order.setOrderName("rmi充值订单");
		order.setOrderType(5);
		order.setUserId("rmi");
		String orderNo = orderService.createOrder(order); // 执行远程方法
		logger.debug("rmi显示调用结果：" + orderNo);
		context.close();
	}
}

