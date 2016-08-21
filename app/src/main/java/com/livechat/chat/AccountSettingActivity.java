package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 账号设置
 */
public class AccountSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
    }

    /**
     * 第一个Item
     *
     * @param view
     */
    public void doRlItemOne(View view) {
        Intent intent = new Intent(this, CloseOrExitActivity.class);
        startActivity(intent);
    }

    /**
     * 第二个Item
     *
     * @param view
     */
    public void doRlItemTwo(View view) {
        Intent intent = new Intent(this, SwitchAccountActivity.class);
        startActivity(intent);
    }

    /**
     * 第三个Item
     *
     * @param view
     */
    public void doRlItemThree(View view) {
        Intent intent = new Intent(this, ClearChatRecorderActivity.class);
        startActivity(intent);
    }

}
