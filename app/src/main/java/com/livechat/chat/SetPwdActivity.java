package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.livechat.chat.widget.LoadingDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * 设定密码
 */
public class SetPwdActivity extends BaseActivity {

    private EditText etId, etPwd, etConfirmPwd;
    private TextView tvShowNumber;

    private LoadingDialog loadingDialog;
    private String sPhoneNumber = "", sCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pwd);

        initUI();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        tvShowNumber = (TextView) this.findViewById(R.id.tvShowNumber);
        etId = (EditText) this.findViewById(R.id.etId);
        etPwd = (EditText) this.findViewById(R.id.etPwd);
        etConfirmPwd = (EditText) this.findViewById(R.id.etConfirmPwd);

        Intent intent = this.getIntent();
        sPhoneNumber = intent.getStringExtra("PhoneNumber");
        sCode = intent.getStringExtra("Code");
        tvShowNumber.setText("请设定您的密码。您可以使用您的手机号码+" + sPhoneNumber);
    }

    /**
     * 完成设置密码
     *
     * @param view
     */
    public void doFinishSetPwd(View view) {
        String sID = etId.getText().toString().trim();
        String sPwd = etPwd.getText().toString().trim();
        String sConfirmPwd = etConfirmPwd.getText().toString().trim();
        if (TextUtils.isEmpty(sID)) {
            CommonUtil.showTips(this, R.mipmap.warning, "请输入ID");
            return;
        }
        if (TextUtils.isEmpty(sPwd)) {
            CommonUtil.showTips(this, R.mipmap.warning, "请输入密码");
            return;
        }
        if (sPwd.length()<8) {
            CommonUtil.showTips(this, R.mipmap.warning, "密码不能小于8位!");
            return;
        }
        if (TextUtils.isEmpty(sConfirmPwd)) {
            CommonUtil.showTips(this, R.mipmap.warning, "请输入确认密码");
            return;
        }
        if (!sPwd.equals(sConfirmPwd)) {
            CommonUtil.showTips(this, R.mipmap.warning, "两次密码不一致，重新设置");
            return;
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
        // 将数据提交到服务器
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("account", sID);// 用户账号
        params.put("appType", 3);// 应用类型[5.web;4.iOS;3.Android]
        params.put("password", sPwd);// 用户密码
        params.put("phone", sCode + " " + sPhoneNumber);// 区号 手机号
        params.put("sign", CommonUtil.md5Encryption(sID + Constant.Token));// 签名(md5(account + token))
        httpClient.post(Constant.sRegisterUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }

                    String sResponse = String.valueOf(response);
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(sResponse);
                    int code = jsonObject.getInteger("code");

                    if (statusCode == 200) {
                        if (code == 0) {// 注册成功
                            Intent intent = new Intent(SetPwdActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            CommonUtil.showTips(SetPwdActivity.this, R.mipmap.smile, "注册成功");
                        } else {// 用户已存在
                            CommonUtil.showTips(SetPwdActivity.this, R.mipmap.warning, "用户已存在");
                        }
                    } else {
                        CommonUtil.showTips(SetPwdActivity.this, R.mipmap.warning, "请求失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                CommonUtil.showTips(SetPwdActivity.this, R.mipmap.warning, "请求失败");
            }
        });
    }

}
