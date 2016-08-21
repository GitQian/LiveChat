package com.livechat.chat;

import android.os.Bundle;
import android.widget.TextView;

/**
 * 版本
 */
public class VersionActivity extends BaseActivity {

    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        initUI();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        tvVersion = (TextView) this.findViewById(R.id.tvVersion);
    }

}
