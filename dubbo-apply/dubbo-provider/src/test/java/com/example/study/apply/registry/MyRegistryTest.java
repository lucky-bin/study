package com.example.study.apply.registry;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;


/**
 * MyRegistryTest
 * 
 */
public class MyRegistryTest {
	
	@Test
	public void test() {
		ExtensionLoader<RegistryFactory> extensionLoader = 
	            ExtensionLoader.getExtensionLoader(RegistryFactory.class);
		RegistryFactory fact = extensionLoader.getExtension("my");
		// zookeeper://localhost:2181/com.alibaba.dubbo.registry.RegistryService?application=demo-service-provider&client=zkclient&dubbo=2.6.1&export=dubbo%3A%2F%2F192.168.56.1%3A28080%2Fcom.study.dubbo.DemoService%3Fanyhost%3Dtrue%26application%3Ddemo-service-provider%26bind.ip%3D192.168.56.1%26bind.port%3D28080%26dubbo%3D2.6.1%26generic%3Dfalse%26interface%3Dcom.study.dubbo.DemoService%26methods%3Dtest%2Csay%26pid%3D20296%26serialization%3Dfastjson%26side%3Dprovider%26timestamp%3D1556003572198&pid=20296&timestamp=1556003572188
		URL url = URL.valueOf("zookeeper://localhost:2181/com.alibaba.dubbo.registry.RegistryService?application=demo-service-provider&client=zkclient&dubbo=2.6.1&export=dubbo%3A%2F%2F192.168.56.1%3A28080%2Fcom.study.dubbo.DemoService%3Fanyhost%3Dtrue%26application%3Ddemo-service-provider%26bind.ip%3D192.168.56.1%26bind.port%3D28080%26dubbo%3D2.6.1%26generic%3Dfalse%26interface%3Dcom.study.dubbo.DemoService%26methods%3Dtest%2Csay%26pid%3D20296%26serialization%3Dfastjson%26side%3Dprovider%26timestamp%1556006021065&pid=20296&timestamp=1556006021065");
		Registry rgt = fact.getRegistry(url);
		// dubbo://192.168.56.1:28080/com.study.dubbo.DemoService?anyhost=true&application=demo-service-provider&dubbo=2.6.1&generic=false&interface=com.study.dubbo.DemoService&methods=test,say&pid=20296&serialization=fastjson&side=provider&timestamp=1556003572198
		URL rigstUrl = URL.valueOf("dubbo://192.168.56.1:28080/com.study.dubbo.DemoService?anyhost=true&application=demo-service-provider&dubbo=2.6.1&generic=false&interface=com.study.dubbo.DemoService&methods=test,say&pid=20296&serialization=fastjson&side=provider&timestamp=1556006021065");
		rgt.register(rigstUrl);
		rgt.subscribe(rigstUrl, new NotifyListener() {
			@Override
			public void notify(List<URL> urls) {
				System.out.println("监听到变化："+urls);
			}
		});
		rgt.unregister(rigstUrl);
		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

