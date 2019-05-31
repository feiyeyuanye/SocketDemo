package com.example.socketapplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 实现一个echo 服务，就是客户端向服务端写入任意数据，服务器都将数据原封不动地写回给客户端。
 * 使用Socket连接服务器
 *
 * 直接右键运行此文件
 * Created by xwxwaa on 2019/5/30.
 */

public class EchoClient {

    private final Socket socket;

    public EchoClient(String host,int port) throws IOException {
        // 创建Socket并连接服务器
        // 内核会创建一个Socket，并且Socket会对它执行connect，发起对服务端的连接。
        // 因为Socket API其实是TCP层的封装，所以connect后，内核会发送一个SYN给服务端。
        socket= new Socket(host,port);
    }

    public void run() throws IOException {
        // 和服务端进行通信
        // 通过 socket 获取输入/输出流进行通信
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 在读取用户输入的同时，我们又想读取服务器的响应。
                // 所以，这里创建了一个线程来读服务器的响应。
                readResponse();
            }
        });
        thread.start();

        OutputStream outputStream = socket.getOutputStream();
        byte[] buffer = new byte[1024];
        int n ;
        // 从键盘读出一个字符，然后返回它的Unicode码
        // 用System.in.read()时，在键盘上按下的任何一个键都会被当做是输入值，包括Enter键也会被当做是一个值！
        while ((n = System.in.read(buffer))>0){
            outputStream.write(buffer,0,n);
        }
    }


    private void readResponse(){
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int n ;
            while ((n=inputStream.read(buffer))>0){
                System.out.write(buffer,0,n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] argv){
        try {
            // 由于服务端运行在同一主机，使用localhost
            EchoClient echoClient = new EchoClient("localhost",9876);
            echoClient.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
