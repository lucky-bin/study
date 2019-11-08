package com.example.study.simple.test;

import com.example.study.StudyApplication;
import com.example.study.simple.RpcServer;
import org.junit.jupiter.api.Test;

/**
 * ServerTest
 * 
 */
public class ServerTest extends StudyApplication {
	
	@Test
	public void startServer() {
		RpcServer server = new RpcServer();
		server.start(9998, "com.example.study.simple.example");
	}
}

