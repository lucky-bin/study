package edu.dongnao.study.dubbo.provider;


import org.apache.dubbo.config.annotation.Service;

import edu.dongnao.study.dubbo.DemoService;

/**
 * 示例接口实现类
 * DemoServiceImpl
 */
@Service
public class DemoServiceImpl implements DemoService {
	
	// 示例方法的实现
	public String sayHello(String name) {
		System.out.println("*********************** " + name);
		return "Hello " + name;
	}

	@Override
	public String sayGood(String name) {
		System.out.println("You are the best! " + name);
		return "You are the best! " + name;
	}

	@Override
	public String sayBad(String name) {
		System.out.println("you are good! " + name);
		return "I'm sorry to hear that. "+name;
	}
}
