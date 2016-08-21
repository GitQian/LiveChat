package com.livechat.chat;

import android.os.Bundle;
import android.view.View;

/**
 * 通用
 */
public class GeneralActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
    }

    /**
     * 多语言
     *
     * @param view
     */
    public void doRlMultilingual(View view) {

    }

    /**
     * 字体大小
     *
     * @param view
     */
    public void doRlFontSize(View view) {

    }

    /**
     * 清空聊天记录
     *
     * @param view
     */
    public void doEmptyChatRecord(View view) {

    }

}
