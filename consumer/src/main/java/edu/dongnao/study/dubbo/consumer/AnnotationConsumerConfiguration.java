package edu.dongnao.study.dubbo.consumer;

import edu.dongnao.study.dubbo.DemoService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


//@Configuration
@EnableDubbo(scanBasePackages = "edu.dongnao.study.dubbo.consumer")
@PropertySource("classpath:/dubbo/dubbo-consumer.properties")
@ComponentScan(value = { "edu.dongnao.study.dubbo.consumer" })
public class AnnotationConsumerConfiguration {

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				AnnotationConsumerConfiguration.class);
		context.start();
		final DemoService annotationAction = context.getBean(DemoService.class);
		String hello = annotationAction.sayHello("world");
		System.out.println(hello);
		context.close();
	}
}
