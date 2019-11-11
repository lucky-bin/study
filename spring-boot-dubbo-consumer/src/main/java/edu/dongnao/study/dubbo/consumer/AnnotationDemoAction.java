package edu.dongnao.study.dubbo.consumer;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dongnao.study.dubbo.DemoService;

@RestController
public class AnnotationDemoAction {

	@Reference
	private DemoService demoService;

	@RequestMapping("/hello")
	public String doSayHello(String name) {
		return demoService.sayHello(name);
	}
}
