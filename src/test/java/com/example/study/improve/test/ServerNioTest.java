package com.example.study.improve.test;

import java.io.IOException;

import com.example.study.StudyApplication;
import com.example.study.improve.RpcNioServer;
import org.junit.jupiter.api.Test;


/**
 * ServerTest
 * 
 */
public class ServerNioTest extends StudyApplication {
	
	@Test
	public void startServer() throws IOException {
		RpcNioServer server = new RpcNioServer(9998, "com.example.study.improve.example");
		server.start();
	}
}

