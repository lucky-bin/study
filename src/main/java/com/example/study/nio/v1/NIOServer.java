package com.example.study.nio.v1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * NIO 不包含 selector  实现网络通信
 */
public class NIOServer {

    // 存放链接
    private static ArrayList<SocketChannel> channels = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        System.out.println("启动成功");
        while (true){
            // 获取新的TCP链接通道
            SocketChannel socketChannel = serverSocketChannel.accept();
            // 如果没有链接建立,就从未断开的链接里面去进行获取
            if (socketChannel != null) {
                System.out.println("收到新连接 : " + socketChannel.getRemoteAddress());
                socketChannel.configureBlocking(false);
                channels.add(socketChannel);
            } else {
                Iterator<SocketChannel> iterator = channels.iterator();
                while (iterator.hasNext()) {
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
                        iterator.remove();
                    } catch (Exception e) {
                        e.printStackTrace();
                        iterator.remove();
                    }
                }
            }
        }
    }
}
