package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 已登录过的
 */
public class LoggedInActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
    }

    /**
     * 登录
     *
     * @param view
     */
    public void doLoggedIn(View view) {

    }

    /**
     * 忘记密码
     *
     * @param view
     */
    public void doForgetPwd(View view) {
        Intent intent = new Intent(this, ForgetPwdActivity.class);
        startActivity(intent);
    }

    /**
     * 注册账号
     *
     * @param view
     */
    public void doRegisterAccount(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 问题
     *
     * @param view
     */
    public void doQuestion(View view) {

    }

}
