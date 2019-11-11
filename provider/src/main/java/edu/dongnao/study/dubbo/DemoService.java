package edu.dongnao.study.dubbo;

/**
 * 示例接口，用于dubbo服务端服务实现，消费者端进行引用。
 * DemoService
 */
public interface DemoService {
	/**
	 * 示例方法
	 * @param name
	 * @return
	 */
	String sayHello(String name);
	
	String sayGood(String name);
	
	String sayBad(String name);
}
