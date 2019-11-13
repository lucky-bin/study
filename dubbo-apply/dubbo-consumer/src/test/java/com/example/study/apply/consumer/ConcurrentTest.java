package com.example.study.apply.consumer;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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
public class ConcurrentTest {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void test() {
		
		// 消费者数
		int consummerNum = 5;
		// 并发数
		int requestSize = 2;
		
		CyclicBarrier requestBarrier = new CyclicBarrier(requestSize * consummerNum);
		// 多线程模拟高并发
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer-thread-model.xml");
		context.start();
		final OrderService orderService = (OrderService) context.getBean("orderService"); // 获取远程服务代理
		for (int i = 0; i < consummerNum; i++) {
			new Thread(new Runnable() {
				public void run() {
					// 模拟分布式集群的场景
					System.out.println(Thread.currentThread().getName() + "---------我准备好---------------");
					for(int i =0; i < requestSize; i++) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									// 等待service台服务，requestSize个请求 一起出发
									requestBarrier.await();
								} catch (InterruptedException | BrokenBarrierException e) {
									e.printStackTrace();
								}
								OrderModel order = new OrderModel();
								order.setOrderName("充值订单");
								order.setOrderType(1);
								order.setUserId("12306");
								String orderNo = orderService.createOrder(order); // 执行远程方法
								logger.debug("显示调用结果：" + orderNo);
							}
						}).start();
					}
				}
			}).start();
		}
		context.close();
	}
}

