package com.example.study.nio.v2;

import lombok.val;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * nio  selector 方式实现
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.configureBlocking(false);
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        System.out.println("启动成功");
        while (true) {
            // 启动Selector    select 是阻塞方法,直到有事件通知才会执行下面的代码
            // 如果 select 传入了时间  或者调用了 selectNow 需要判断返回值是不是大于0
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            if(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println(String.format("收到新链接： %s", socketChannel));
                } else if(key.isReadable()) {
                    try {


                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        // 判断通道是否打开  以及数据是否有
                        while (socketChannel.isOpen() && socketChannel.read(byteBuffer) != -1) {
                            // 判断是否还有数据要读取
                            if (byteBuffer.position() > 0) {
                                break;
                            }
                        }
                        // 是否还有数据
                        if (byteBuffer.position() == 0) {
                            continue;
                        }
                        // 转换模式
                        byteBuffer.flip();
                        // 使用 remaining 获取真实位置
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        System.out.println(String.format("接收到了来自 {%s} 数据： %s", socketChannel.getRemoteAddress(), new String(bytes)));

                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Length: 11\r\n\r\n" +
                                "Hello World";
                        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                        while (buffer.hasRemaining()) {
                            socketChannel.write(buffer);
                        }
                    }catch (Exception e){
                        key.cancel();
                    }
                }
            }

        }
    }
}
