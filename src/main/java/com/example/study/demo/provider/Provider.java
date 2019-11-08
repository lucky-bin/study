package com.example.study.demo.provider;

import com.example.study.server.RpcBootstrap;

public class Provider {
	
	/*
	 * 运行代码依赖zk地址，在app.properties中配置即可
	 * 配置项：zk.address=
	 */
	public static void main(String[] args) throws Exception {
		RpcBootstrap bootstrap = new RpcBootstrap();
		bootstrap.start("com.example.study.demo");
		System.in.read(); // 按任意键退出
	}
}
