package com.example.socketapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    /**
     * 长连接 服务端
     */
    private LongEchoServer mEchoServer;
    /**
     * 长连接 客户端
     */
    private LongEchoClient mEchoClient;

    private EditText mMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int port = 9877;
        mEchoServer = new LongEchoServer(port);
        mEchoServer.run();

        mEchoClient = new LongEchoClient("localhost", port);

        mMsg = findViewById(R.id.msg);
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mMsg.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    return;
                }
                mEchoClient.send(msg);
                mMsg.setText("");
            }
        });
    }
}
