package com.example.study.demo.provider;

import com.example.study.demo.DemoService;
import com.example.study.server.Service;

@Service(DemoService.class)
public class DemoServiceImpl implements DemoService {
	/**
	 * 代码实现
	 */
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
