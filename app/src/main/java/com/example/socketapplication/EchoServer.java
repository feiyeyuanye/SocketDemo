package com.example.socketapplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * 实现一个echo 服务，就是客户端向服务端写入任意数据，服务器都将数据原封不动地写回给客户端。
 * 1.创建ServerSocket并监听客户连接
 *
 * 直接右键运行此文件
 * Created by xwxwaa on 2019/5/30.
 */

public class EchoServer {

    private final ServerSocket serverSocket;

    public static void main(String[] argv){
        EchoServer echoServer = null;
        try {
            echoServer = new EchoServer(9876);
            echoServer.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EchoServer(int port) throws IOException {
        // 1.创建一个ServerSocket，并监听端口 port
        // 内核会创建一个Socket，然后ServerSocket对这个Socket调用listen函数，开始监听客户的连接。
        // Socket 并不仅仅使用端口号来区别不同的 socket 实例，而是使用 <peer addr:peer port, local addr:local port> 这个四元组。
        // 关于端口号：ServerSocket 长这样：<*:*, *:9876>。意思是，可以接受任何的客户端，和本地任何 IP。
        serverSocket = new ServerSocket(port);
    }

    public void run() throws IOException {
        // 2.开始接受客户连接
        // 服务端的主机接收到SYN后，会创建一个新的Socket，这个新的Socket会跟客户端继续执行三次握手过程。
        // 三次握手完成后，执行的serverSocket.accept()会返回一个实例，这个Socket就是上一步内核创建的。
        // 关于端口号：accept 返回的 Socket 则是这样：<127.0.0.1:xxxx, 127.0.0.1:9877>，其中xxxx 是客户端的端口号。
        Socket socket = serverSocket.accept();
        handleChilent(socket);
    }

    private void handleChilent(Socket socket) throws IOException {
        // 3.使用Socket进行通信
        // 服务端，通过socket获取输入输出流进行通信
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        // 不停的读取输入数据，然后写回给客户端。
        while (( n = inputStream.read(buffer))>0){
            outputStream.write(buffer,0,n);
        }
    }
}
