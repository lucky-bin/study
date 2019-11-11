package edu.dongnao.study.dubbo.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;


/**
 * 使用dubbo @Service 注解方式配置服务实现服务提供
 * AnnotationProviderConfiguration
 */
//@Configuration
@EnableDubbo(scanBasePackages = "edu.dongnao.study.dubbo.provider ")
@PropertySource("classpath:/dubbo/dubbo-provider.properties")
public class AnnotationProviderConfiguration {
	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				AnnotationProviderConfiguration.class);
		context.start();
		System.in.read(); // 按任意键退出
		context.close();
	}
}
