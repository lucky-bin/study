package com.example.study.improve.test;

import com.example.study.StudyApplication;
import com.example.study.improve.RpcClientProxy;
import com.example.study.improve.example.Student;
import com.example.study.improve.example.StudentService;
import org.junit.jupiter.api.Test;


/**
 * ClientTest
 * 
 */
public class ClientTest extends StudyApplication {
	
	@Test
	public void test() {
		// 本地没有接口实现，通过代理获得接口实现实例
		RpcClientProxy proxy = new RpcClientProxy();
		StudentService service = proxy.getProxy(StudentService.class);
		
		System.out.println(service.getInfo());
		
		Student student = new Student();
		student.setAge(23);
		student.setName("hashmap");
		student.setSex("男");
		System.out.println(service.printInfo(student));
	}
	
}

