package com.example.study.apply.provider;

import org.apache.thrift.TException;

import com.example.study.apply.thrift.HelloThrift.Iface;

/**
 * HelloThriftImpl
 * 
 */
public class HelloThriftImpl implements Iface {

	@Override
	public String sayHello(String para) throws TException {
		return "hello "+para+", this is thrift.";
	}

}

