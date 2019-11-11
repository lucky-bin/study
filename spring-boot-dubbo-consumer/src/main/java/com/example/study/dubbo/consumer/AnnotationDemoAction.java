package com.example.study.dubbo.consumer;

import com.example.study.dubbo.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnnotationDemoAction {

	@Reference
	private DemoService demoService;

	@RequestMapping("/hello")
	public String doSayHello(String name) {
		return demoService.sayHello(name);
	}
}
