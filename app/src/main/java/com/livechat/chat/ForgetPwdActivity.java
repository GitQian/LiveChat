package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.livechat.chat.widget.LoadingDialog;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 忘记密码
 */
public class ForgetPwdActivity extends BaseActivity {

    private EditText etPhoneNumber;
    private TextView tvAdrCountry, tvCode;

    private LoadingDialog loadingDialog;
    private String sCode = "";
    private EventHandler eh;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.SUBMIT_SUCCESS_CODE:
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    CommonUtil.showTips(getApplicationContext(), R.mipmap.smile, "提交验证码成功");
                    break;
                case Constant.GET_CODE_SUCCESS_CODE:
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    Intent intent = new Intent(getApplicationContext(), VerCodeLoginActivity.class);
                    intent.putExtra("sPhoneNumber", etPhoneNumber.getText().toString().trim());
                    intent.putExtra("sCode", sCode);
                    intent.putExtra("sign", 1);
                    startActivity(intent);
                    CommonUtil.showTips(getApplicationContext(), R.mipmap.smile, "验证码已发送");
                    break;
                case Constant.COUNTRIES_LIST_CODE:
                    // 返回支持发送验证码的国家列表
                    break;
                case Constant.SUBMIT_FAILURE_ERROR:
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    CommonUtil.showTips(getApplicationContext(), R.mipmap.error, "验证码错误");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        initUI();
        initSMSSDK();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_tab_pay_close, R.anim.activity_tab_pay_close);
    }
    /**
     * 初始化控件UI
     */
    private void initUI() {
        etPhoneNumber = (EditText) this.findViewById(R.id.etPhoneNumber);
        tvAdrCountry = (TextView) this.findViewById(R.id.tvAdrCountry);
        tvCode = (TextView) this.findViewById(R.id.tvCode);
    }

    /**
     * 初始化短信SDK
     */
    private void initSMSSDK() {
        // 短信
        SMSSDK.initSDK(getApplicationContext(), Constant.APPKEY, Constant.APPSECRET);
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {// 回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Message message = Message.obtain();
                        message.what = Constant.SUBMIT_SUCCESS_CODE;
                        handler.sendMessage(message);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {// 获取验证码成功
                        Message message = Message.obtain();
                        message.what = Constant.GET_CODE_SUCCESS_CODE;
                        handler.sendMessage(message);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        // 返回支持发送验证码的国家列表
                        Message message = Message.obtain();
                        message.what = Constant.COUNTRIES_LIST_CODE;
                        handler.sendMessage(message);
                    }
                } else {
                    Message message = Message.obtain();
                    message.what = Constant.SUBMIT_FAILURE_ERROR;
                    handler.sendMessage(message);
                    ((Throwable) data).printStackTrace();
//                    System.out.print("===>>>" + ((Throwable) data).getMessage());
                }
            }
        };
        SMSSDK.registerEventHandler(eh);// 注册短信回调
    }

    /**
     * 提交
     *
     * @param view
     */
    public void doSubmitPwd(View view) {
        String sPhoneNumber = etPhoneNumber.getText().toString().trim();
        sCode = (tvCode.getText().toString()).substring(1, (tvCode.getText().toString()).length()).trim();
        if (TextUtils.isEmpty(sPhoneNumber)) {
            CommonUtil.showTips(this, R.mipmap.warning, "请填写手机号");
            return;
        }
//        if (!CommonUtil.isVerification(sPhoneNumber, "phone")) {
//            CommonUtil.showTips(this, R.mipmap.warning, "手机号不正确");
//            return;
//        }
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在提交，请稍后...");
        loadingDialog.show();
        if (!CommonUtil.isNetworkAvailable(this)) {
            CommonUtil.showTips(this, R.mipmap.warning, "网络不可用");
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            return;
        }

        try {
            // 获取验证码
            SMSSDK.getVerificationCode(sCode, sPhoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onRestart() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        super.onRestart();
    }

    /**
     * 国家
     *
     * @param view
     */
    public void doRlCountry(View view) {
        Intent intent = new Intent(this, CountyCodeActivity.class);
        startActivityForResult(intent, Constant.COUNTY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.COUNTY_CODE) {
                Bundle bundle = data.getBundleExtra("CountryBean");
                tvAdrCountry.setText(bundle.getString("Country"));
                tvCode.setText("+" + bundle.getString("Code"));
            }
        }
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(eh);
        super.onDestroy();
    }
    /**
     * 关闭键盘
     *
     * @param view
     */
    public void doFLayout(View view) {
        View view1 = getWindow().peekDecorView();
        if (view1 != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
