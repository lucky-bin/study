package com.example.study.apply.spi;

import org.junit.Test;

import org.apache.dubbo.common.extension.ExtensionLoader;


/**
 * DubboSPITest
 * 
 */
public class DubboSpiTest {
	
	@Test
    public void sayHello() throws Exception {
        ExtensionLoader<Robot> extensionLoader = 
            ExtensionLoader.getExtensionLoader(Robot.class);
        
        Robot defautRobot = extensionLoader.getDefaultExtension();
        defautRobot.sayHello();
        
        Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
        optimusPrime.sayHello();
        
        Robot bumblebee = extensionLoader.getExtension("bumblebee");
        bumblebee.sayHello();
    }
}

