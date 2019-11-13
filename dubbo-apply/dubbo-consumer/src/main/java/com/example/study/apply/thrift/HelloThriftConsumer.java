package com.example.study.apply.thrift;

import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * 客户端启动方式示例
 * Consumer
 */
public class HelloThriftConsumer {
	/**
	 * 通过spring xml启动方式示例
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
		context.start();

		HelloThrift.Iface helloFace = (HelloThrift.Iface) context.getBean("helloThrift"); // 获取远程服务代理
		String word = helloFace.sayHello("world"); // 执行远程方法
		System.out.println(word); // 显示调用结果
		System.in.read();
		context.close();
	}
}
