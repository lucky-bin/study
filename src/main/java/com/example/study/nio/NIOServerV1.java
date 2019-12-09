package com.example.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOServerV1 {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//        Selector selector = Selector.open();
        serverSocketChannel.configureBlocking(false);
//        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        System.out.println("启动成功");
        while (true){
//            selector.select();
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                System.out.println("收到新连接 : " + socketChannel.getRemoteAddress());
                socketChannel.configureBlocking(false);
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    while (socketChannel.isOpen() && socketChannel.read(byteBuffer) != -1) {
                        if (byteBuffer.position() > 0) {
                            break;
                        }
                    }
                    if (byteBuffer.position() == 0) {
                        continue;
                    }
                    byteBuffer.flip();
                    byte[] content = new byte[byteBuffer.limit()];
                    byteBuffer.get(content);
                    System.out.println(new String(content));
                    System.out.println("收到数据,来自：" + socketChannel.getRemoteAddress());

                    // 响应结果 200
                    String response = "HTTP/1.1 200 OK\r\n" +
                            "Content-Length: 11\r\n\r\n" +
                            "Hello World";
                    ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                    while (buffer.hasRemaining()) {
                        socketChannel.write(buffer);// 非阻塞
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
