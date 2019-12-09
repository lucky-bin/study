package com.example.study.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class NIOServer {

    private ServerSocketChannel serverSocketChannel;
    private ReactDemo[] mainReactDemo;
    private ReactDemo[] subReactDemo;

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 创建 selector
        Selector selector = Selector.open();
        serverSocketChannel.configureBlocking(false);
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
    }

    class ReactDemo extends Thread{


    }
}
