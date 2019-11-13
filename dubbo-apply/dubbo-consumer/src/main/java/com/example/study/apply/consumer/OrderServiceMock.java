package com.example.study.apply.consumer;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.study.apply.facade.OrderService;
import com.example.study.apply.model.OrderModel;

/**
 * OrderServiceImpl
 * 
 */
public class OrderServiceMock implements OrderService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private AtomicInteger orderNo = new AtomicInteger(0);
	
	
	@Override
	public String createOrder(OrderModel order) {
		logger.debug("这是一个mock实现："+order.toString());
		String newOrderNo = "mock-"+String.valueOf(orderNo.incrementAndGet());
		
		// 执行业务代码
		// 。。。。。
		try {
			Thread.sleep(200L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		order.setOrderNo(newOrderNo);
		logger.debug("mock处理结果为："+order.toString());
		return newOrderNo;
	}


	@Override
	public OrderModel getOrder(String orderNo) {
		OrderModel order = new OrderModel();
		order.setOrderName("Mock订单");
		order.setOrderType(3);
		order.setUserId("12308");
		order.setOrderNo(orderNo);
		return order;
	}

}

