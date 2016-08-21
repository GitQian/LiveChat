package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 收发消息
 */
public class SendReceiveMessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receive_message);
    }

    /**
     * 开启或关闭新消息提醒
     *
     * @param view
     */
    public void doHowNewWarm(View view) {
        Intent intent = new Intent(this, StartOrCloseNewsWarmActivity.class);
        startActivity(intent);
    }

}
