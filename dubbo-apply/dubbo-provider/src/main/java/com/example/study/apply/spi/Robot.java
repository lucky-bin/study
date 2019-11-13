package com.example.study.apply.spi;

import org.apache.dubbo.common.extension.SPI;

/**
 * Robot
 * 
 */
@SPI("bumblebee")
public interface Robot {
	void sayHello();
}

