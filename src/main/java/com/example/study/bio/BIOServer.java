package com.example.study.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {

    public static void main(String[] args) throws IOException {
        // 开启socket 服务端
        ServerSocket serverSocket = new ServerSocket(8080);
        // 判断服务端是否关闭
        while (!serverSocket.isClosed()) {
            // 阻塞获取客户端连接
            try(Socket socket = serverSocket.accept()) {
                // 获取客户端传输内容
                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String msg;
                while ((msg = bufferedReader.readLine()) != null) {
                    // 这里客户端得需要传入 bye ,否则会一直循环
                    if (msg.isEmpty() || "bye".equalsIgnoreCase(msg)) {
                        break;
                    }
                    System.out.println(msg);
                }

                // 响应给客户端的数据
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write("HTTP/1.1 200 OK\r\n".getBytes());
                outputStream.write("Content-Length: 11\r\n\r\n".getBytes());
                outputStream.write("Hello World".getBytes());
                outputStream.flush();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        serverSocket.close();
    }
}
