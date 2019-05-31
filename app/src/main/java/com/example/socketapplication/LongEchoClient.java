package com.example.socketapplication;

import android.util.Log;

/**
 *  长连接 客户端
 *
 * Created by xwxwaa on 2019/5/31.
 */

public class LongEchoClient {

    private static final String TAG = "EchoClient";

    private final LongLiveSocket mLongLiveSocket;

    public LongEchoClient(String host, int port) {
        // 实例化。依次传入主机地址，端口号，并实现写数据的回调，和错误回调（设置为需要重连）
        mLongLiveSocket = new LongLiveSocket(
                host, port,
                (data, offset, len) -> Log.e(TAG, "EchoClient: received: " + new String(data, offset, len)),
                () -> true);
    }

    /**
     * 客户端发送的信息
     */
    public void send(String msg) {
        mLongLiveSocket.write(msg.getBytes(), new LongLiveSocket.WritingCallback() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "onSuccess: ");
            }

            @Override
            public void onFail(byte[] data, int offset, int len) {
                Log.e(TAG, "onFail: fail to write: " + new String(data, offset, len));
                // 失败，则重新发送
                mLongLiveSocket.write(data, offset, len, this);
            }
        });
    }

    public void close() {
        mLongLiveSocket.close();
    }
}