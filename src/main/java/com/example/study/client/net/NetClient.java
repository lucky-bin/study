package com.example.study.client.net;

import com.example.study.discovery.ServiceInfo;

public interface NetClient {
	byte[] sendRequest(byte[] data, ServiceInfo sinfo);
}
