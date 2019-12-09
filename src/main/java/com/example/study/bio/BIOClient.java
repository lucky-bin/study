package com.example.study.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

public class BIOClient {
    private static final String charsetName = "utf-8";

    // 服务端读取到这里会不再读取
    private static final String endding = "bye\r\n";

    public static void main(String[] args) throws IOException {
        // 连接服务端
        Socket socket = new Socket("127.0.0.1", 8080);

        // 给服务端发送数据
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("Hello World Socket\r\n".getBytes(charsetName));
        outputStream.write(endding.getBytes(charsetName));

        // 获取服务端响应的数据
        InputStream inputStream = socket.getInputStream();
        byte[] data = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(data)) > 0) {
            System.out.println(new String(data));
        }
        socket.close();
    }
}
