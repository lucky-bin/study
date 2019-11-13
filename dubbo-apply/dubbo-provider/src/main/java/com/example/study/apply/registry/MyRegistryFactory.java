package com.example.study.apply.registry;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;
import org.apache.dubbo.registry.support.AbstractRegistryFactory;


/**
 * DubboRegistryFactory
 * 
 */
public class MyRegistryFactory extends AbstractRegistryFactory implements RegistryFactory {

	@Override
	public Registry getRegistry(URL url) {
		System.out.println("获取myRegistry");
		return super.getRegistry(url);
	}

	@Override
	protected Registry createRegistry(URL url) {
		return new MyRegistry(url);
	}

}

