package com.example.study.nio.v3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * nio reactor 模型方式实现
 */
public class NIOServer {

    // 处理业务操作的线程
    private static ExecutorService workPool = Executors.newCachedThreadPool();
    private ServerSocketChannel serverSocketChannel;
    // 处理连接线程
    private Reactor[] mainReactors = new Reactor[1];
    // io 处理线程
    private Reactor[] subReactors = new Reactor[4];

    public static void main(String[] args) throws IOException {
        NIOServer nioServer = new NIOServer();
        nioServer.newGroup();
        nioServer.initAndRegister();
        nioServer.bind();
    }

    /**
     * 初始化连接信息
     * @throws IOException
     */
    private void newGroup() throws IOException {
        // 创建IO线程,负责处理客户端连接以后socketChannel的IO读写
        for (int i = 0; i < subReactors.length; i++) {
            subReactors[i] = new Reactor() {
                @Override
                public void handler(SelectableChannel channel) throws IOException {
                    // work线程只负责处理IO处理，不处理accept事件
                    SocketChannel socketChannel = (SocketChannel) channel;
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    while (socketChannel.isOpen() && socketChannel.read(byteBuffer) != -1) {
                        // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
                        if (byteBuffer.position() > 0) {
                            break;
                        }
                    }
                    if (byteBuffer.position() == 0){
                        // 如果没数据了, 则不继续后面的处理
                        return;
                    }
                    byteBuffer.flip();
                    byte[] content = new byte[byteBuffer.limit()];
                    byteBuffer.get(content);
                    System.out.println(new String(content));
                    System.out.println(Thread.currentThread().getName() + "收到数据,来自：" + socketChannel.getRemoteAddress());

                    // TODO 业务操作 数据库、接口...
                    workPool.submit(() -> {
                    });

                    // 响应结果 200
                    String response = "HTTP/1.1 200 OK\r\n" +
                            "Content-Length: 11\r\n\r\n" +
                            "Hello World";
                    ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                    while (buffer.hasRemaining()) {
                        socketChannel.write(buffer);
                    }
                }
            };
        }

        // 创建mainReactor线程, 只负责处理serverSocketChannel
        for (int i = 0; i < mainReactors.length; i++) {
            mainReactors[i] = new Reactor() {
                AtomicInteger incr = new AtomicInteger(0);

                @Override
                public void handler(SelectableChannel channel) throws IOException {
                    // 只做请求分发，不做具体的数据读取
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) channel;
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 收到连接建立的通知之后，分发给I/O线程继续去读取数据
                    int index = incr.getAndIncrement() % subReactors.length;
                    Reactor reactor = subReactors[index];
                    reactor.doStart();
                    SelectionKey selectionKey = reactor.register(socketChannel);
                    selectionKey.interestOps(SelectionKey.OP_READ);
                    System.out.println(Thread.currentThread().getName() + "收到新连接 : " + socketChannel.getRemoteAddress());
                }
            };
        }
    }

    /**
     * 初始化服务端信息
     * @throws IOException
     */
    public void initAndRegister() throws IOException {
        // 1、 创建ServerSocketChannel
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        // 2、 将serverSocketChannel注册到selector
        int index = new Random().nextInt(mainReactors.length);
        mainReactors[index].doStart();
        SelectionKey selectionKey = mainReactors[index].register(serverSocketChannel);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
    }

    /**
     * 端口绑定
     * @throws IOException
     */
    public void bind() throws IOException {
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
    }

    /**
     * reaactor 线程模型
     */
    abstract class Reactor extends Thread {

        Selector selector;

        volatile boolean running = false;

        public Reactor() throws IOException {
            // 初始化一个客户端连接的selector
            selector = Selector.open();
        }

        /**
         * 具体的执行逻辑
         * @param selectableChannel
         * @throws IOException
         */
        abstract void handler(SelectableChannel selectableChannel) throws IOException;

        @Override
        public void run() {
            while (true) {
                try {
                    // 阻塞  等待连接
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        int readyOps = key.readyOps();
                        if ((readyOps & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT)) != 0 || readyOps == 0) {
                            try {
                                SelectableChannel channel = (SelectableChannel) key.attachment();
                                channel.configureBlocking(false);
                                handler(channel);
                                if (!channel.isOpen()) {
                                    key.cancel(); // 如果关闭了,就取消这个KEY的订阅
                                }
                            } catch (Exception ex) {
                                key.cancel(); // 如果有异常,就取消这个KEY的订阅
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 注册事件
         * @param channel
         * @return
         * @throws IOException
         */
        private SelectionKey register(SelectableChannel channel) throws IOException {
            return channel.register(selector, 0, channel);
        }

        /**
         * 启动线程
         */
        private void doStart() {
            if (!running) {
                running = true;
                start();
            }
        }
    }
}
