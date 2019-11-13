package com.example.study.apply;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务端启动入口
 * Provider
 */
public class ProviderThriftBootstrap {
	/**
	 * 通过main方法启动服务，手动方式加载spring容器
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		/*
		HelloThrift.Iface face = new HelloThriftImpl();
		String resp = face.sayHello("world");
		System.out.println(resp);
		*/
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("provider-thrift.xml"); context.start();
		System.in.read(); // 按任意键退出 
		context.close();
		
	}
}
