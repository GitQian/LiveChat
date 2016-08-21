package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 帮助与反馈(新)
 */
public class HelpAndFeedbackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_feedback);
    }

    /**
     * 账号设置
     *
     * @param view
     */
    public void doRlAccountSetting(View view) {
        Intent intent = new Intent(this, AccountSettingActivity.class);
        startActivity(intent);
    }

    /**
     * 收发消息
     *
     * @param view
     */
    public void doSendReceiverNews(View view) {
        Intent intent = new Intent(this, SendReceiveMessageActivity.class);
        startActivity(intent);
    }

}
