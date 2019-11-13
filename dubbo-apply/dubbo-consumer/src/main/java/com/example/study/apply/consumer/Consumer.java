package com.example.study.apply.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.study.apply.facade.OrderService;
import com.example.study.apply.model.OrderModel;
/**
 * 客户端启动方式示例
 * Consumer
 */
public class Consumer {
	/**
	 * 通过spring xml启动方式示例
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
		context.start();

		OrderService orderService = (OrderService) context.getBean("orderService"); // 获取远程服务代理
		OrderModel order = new OrderModel();
		order.setOrderName("充值订单");
		order.setOrderType(1);
		order.setUserId("12306");
		order.setOrderNo("");
		String orderNo = orderService.createOrder(order); // 执行远程方法
		System.out.println(orderNo); // 显示调用结果

		System.out.println();
		System.out.println(orderService);
		System.in.read();
		context.close();
	}
}
