package com.example.socketapplication;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 长连接 服务端
 *
 * Created by xwxwaa on 2019/5/31.
 */

public class LongEchoServer {
    private static final String TAG = "EchoServer";

    private final int mPort;
    private final ExecutorService mExecutorService;

    public LongEchoServer(int port) {
        mPort = port;
        // 固定大小的线程池
        mExecutorService = Executors.newFixedThreadPool(4);
    }

    public void run() {
        mExecutorService.submit(() -> {
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(mPort);
            } catch (IOException e) {
                Log.e(TAG, "run: ", e);
                return;
            }
            try {
                while (true) {
                    Socket client = serverSocket.accept();
                    handleClient(client);
                }
            } catch (IOException e) {
                Log.e(TAG, "run: ");
            }
        });
    }

    private void handleClient(Socket socket) {
        mExecutorService.submit(() -> {
            try {
                doHandleClient(socket);
            } catch (IOException e) {
                Log.e(TAG, "handleClient: ", e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e(TAG, "handleClient: ", e);
                }
            }
        });
    }

    private void doHandleClient(Socket socket) throws IOException {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        while ((n = in.read(buffer)) > 0) {
            out.write(buffer, 0, n);
        }
    }

}
