package com.example.study.apply;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务端启动入口
 * Provider
 */
public class ProviderRmiBootstrap {
	/**
	 * 通过main方法启动服务，手动方式加载spring容器
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.setProperty("sun.rmi.transport.tcp.responseTimeout", "3000");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("provider-rmi.xml");
		context.start();
		System.in.read(); // 按任意键退出
		context.close();
	}
}
