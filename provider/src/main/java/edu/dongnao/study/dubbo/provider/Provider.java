package edu.dongnao.study.dubbo.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务端启动入口
 * Provider
 */
public class Provider {
	/**
	 * 通过main方法启动服务，手动方式加载spring容器
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("provider.xml");
		context.start();
		System.in.read(); // 按任意键退出
		context.close();
	}
}
