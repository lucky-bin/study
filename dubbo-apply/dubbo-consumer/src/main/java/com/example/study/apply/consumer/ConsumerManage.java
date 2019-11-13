package com.example.study.apply.consumer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.study.apply.facade.OrderService;
import com.example.study.apply.model.OrderModel;
/**
 * 客户端启动方式示例
 * Consumer
 */
public class ConsumerManage {
	final static Executor executor = Executors.newFixedThreadPool(5);
	final static Logger logger = LoggerFactory.getLogger(ConsumerManage.class);
	/**
	 * 通过spring xml启动方式示例
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
		context.start();

		OrderService orderService = (OrderService) context.getBean("orderService"); // 获取远程服务代理
		
		// 写操作
		executor.execute(()->{
			while(true) {
				OrderModel order = new OrderModel();
				order.setOrderName("充值订单");
				order.setOrderType(1);
				order.setUserId("12306");
				String orderNo = orderService.createOrder(order); // 执行远程方法
				logger.debug("创建订单成功，获得编号："+orderNo);
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		// 读操作
		executor.execute(()->{
			while(true) {
				OrderModel order = orderService.getOrder("123"); // 执行远程方法
				logger.debug("查询订单成功，获得订单内容："+order);
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		Thread.currentThread().join();
		
		context.close();
	}
}
