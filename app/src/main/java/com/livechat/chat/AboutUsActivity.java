package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    /**
     * 隐私政策
     *
     * @param view
     */
    public void doPrivacyPolicy(View view) {
        Intent intent = new Intent(this, PublicWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("whetherShowActionBar", true);// 是否显示ActionBar:true:显示;false:隐藏;
        bundle.putString("subTitle", "关于");// 副标题
        bundle.putString("masterTitle", "服务条款和隐私政策");// 主标题
        bundle.putString("otherTitle", "");// 其他标题
        bundle.putString("webViewUrl", "http://www.baidu.com");// Url地址
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
