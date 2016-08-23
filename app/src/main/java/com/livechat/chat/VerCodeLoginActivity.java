package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.livechat.chat.widget.LoadingDialog;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 验证码登录
 */
public class VerCodeLoginActivity extends BaseActivity {

    private static final int VERCODE_SUCCESS = 1;
    private static final int VERCODE_FAIL = 2;
    private EditText etVerifyCode;
    private TextView tvMobilePhoneNum, tvSecond;

    private String sPhoneNumber, sCode;
    private LoadingDialog loadingDialog;
    private TimerTask timerTask;
    private Timer timer;
    private int second = 60, sign;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    second--;
                    if (second < 0) {
                        tvSecond.setText("60");
                        timer.cancel();
                    } else {
                        tvSecond.setText(second + "");
                    }
                    break;

                case VERCODE_SUCCESS:
                    // 跳转
                    Intent intent = new Intent();
                    if (sign == 1) {// 忘记密码
                        intent.setClass(VerCodeLoginActivity.this, SetNewPwdActivity.class);
                    } else if (sign == 2) {// 注册账号
                        intent.setClass(VerCodeLoginActivity.this, SetPwdActivity.class);
                    }
                    intent.putExtra("PhoneNumber", tvMobilePhoneNum.getText().toString());
                    intent.putExtra("Code", sCode);
                    startActivity(intent);
                    VerCodeLoginActivity.this.finish();
                    break;

                case VERCODE_FAIL:
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    Toast.makeText(VerCodeLoginActivity.this, "验证失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            switch (event) {
                case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        Log.i("qiansheng", "验证成功");
                        handler.sendEmptyMessage(1);
                    } else {
                        Log.i("qiansheng", "验证失败");
                        handler.sendEmptyMessage(2);
                    }
                    break;
                case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        Log.i("qiansheng", "获取验证码成功");
                    } else {
                        Log.i("qiansheng", "获取验证码失败");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_code_login);

        initUI();
        //初始化短信SDK，注册短信回调
        SMSSDK.initSDK(getApplicationContext(), Constant.APPKEY, Constant.APPSECRET);
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        Intent intent = this.getIntent();
        sPhoneNumber = intent.getStringExtra("sPhoneNumber");
        sCode = intent.getStringExtra("sCode");
        sign = intent.getIntExtra("sign", 0);

        etVerifyCode = (EditText) this.findViewById(R.id.etVerifyCode);
        tvMobilePhoneNum = (TextView) this.findViewById(R.id.tvMobilePhoneNum);
        tvSecond = (TextView) this.findViewById(R.id.tvSecond);
        tvMobilePhoneNum.setText(sPhoneNumber);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 验证码登录
     *
     * @param view
     */
    public void doVerCodeLogin(View view) {
        String sVerifyCode = etVerifyCode.getText().toString().trim();
        if (TextUtils.isEmpty(sVerifyCode)) {
            CommonUtil.showTips(this, R.mipmap.warning, "请输入验证码");
            return;
        }
        // 验证码提交进行验证
        try {
            SMSSDK.submitVerificationCode(sCode, sPhoneNumber, sVerifyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在操作，请稍后...");
        loadingDialog.show();
        if (!CommonUtil.isNetworkAvailable(this)) {
            CommonUtil.showTips(this, R.mipmap.warning, "网络不可用");
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            return;
        }

    }

    @Override
    protected void onRestart() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        if (timerTask != null) {
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        SMSSDK.unregisterEventHandler(eh);
        super.onDestroy();
    }

}
