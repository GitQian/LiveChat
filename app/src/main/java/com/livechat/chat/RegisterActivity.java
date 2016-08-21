package com.livechat.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
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
 * 注册账号
 */
public class RegisterActivity extends BaseActivity {

    private EditText etPhoneNumber;
    private TextView tvAdr, tvCode, tvAgree;

    private LoadingDialog loadingDialog;
    private String sCode = "", sPhoneNumber = "";
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
                    intent.putExtra("sPhoneNumber", sPhoneNumber);
                    intent.putExtra("sCode", sCode);
                    intent.putExtra("sign", 2);
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_tab_pay_close, R.anim.activity_tab_pay_close);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();
        initSMSSDK();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        etPhoneNumber = (EditText) this.findViewById(R.id.etPhoneNumber);
        tvAdr = (TextView) this.findViewById(R.id.tvAdr);
        tvCode = (TextView) this.findViewById(R.id.tvCode);
        tvAgree = (TextView) this.findViewById(R.id.tvAgree);

        setTvAutoLink();
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
                }
            }
        };
        SMSSDK.registerEventHandler(eh);// 注册短信回调
    }

    /**
     * 注册
     *
     * @param view
     */
    public void doRegister(View view) {
        sPhoneNumber = etPhoneNumber.getText().toString().trim();
        sCode = (tvCode.getText().toString()).substring(1, (tvCode.getText().toString()).length()).trim();
        if (TextUtils.isEmpty(sPhoneNumber)) {
            CommonUtil.showTips(this, R.mipmap.warning, "手机号不能为空");
            return;
        }
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在注册，请稍后...");
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

    /**
     * 国家
     *
     * @param view
     */
    public void doRlCountryCode(View view) {
        Intent intent = new Intent(this, CountyCodeActivity.class);
        startActivityForResult(intent, Constant.COUNTY_CODE);
    }

    /**
     * 关闭键盘
     *
     * @param view
     */
    public void doReLayout(View view) {
        View view1 = getWindow().peekDecorView();
        if (view1 != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 设置链接
     */
    private void setTvAutoLink() {
        SpannableString spannableStr = new SpannableString("《服务条款和隐私政策》");
        spannableStr.setSpan(new URLSpan(""), 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStr.setSpan(new ForegroundColorSpan(Color.parseColor("#3F94FF")), 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAgree.setText(spannableStr);
//        tvAgree.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 服务协议
     *
     * @param view
     */
    public void doTvServiceAgreement(View view) {
        Intent intent = new Intent(this, PublicWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("whetherShowActionBar", true);// 是否显示ActionBar:true:显示;false:隐藏;
        bundle.putString("subTitle", "注册");// 副标题
        bundle.putString("masterTitle", "服务协议");// 主标题
        bundle.putString("otherTitle", "");// 其他标题
        bundle.putString("webViewUrl", "http://www.e8098.com/help_aboutus_GB.cfm");// Url地址
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.COUNTY_CODE) {
                Bundle bundle = data.getBundleExtra("CountryBean");
                tvAdr.setText(bundle.getString("Country"));
                tvCode.setText("+" + bundle.getString("Code"));
            }
        }
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(eh);
        super.onDestroy();
    }

}
