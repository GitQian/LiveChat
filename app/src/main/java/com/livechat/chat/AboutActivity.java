package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    /**
     * 版本
     *
     * @param view
     */
    public void doVersion(View view) {
        Intent intent = new Intent(this, VersionActivity.class);
        startActivity(intent);
    }

    /**
     * 联系我们
     *
     * @param view
     */
    public void doRelationUs(View view) {
        Intent intent = new Intent(this, ContactUsActivity.class);
        startActivity(intent);
    }

}
