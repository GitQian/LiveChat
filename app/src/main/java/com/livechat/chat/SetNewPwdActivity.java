package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

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
 * 设定新密码
 */
public class SetNewPwdActivity extends BaseActivity {

    private EditText etNewPwd, etConfirmNewPwd;

    private LoadingDialog loadingDialog;
    private String sCode = "", sPhoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_pwd);

        initUI();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        etNewPwd = (EditText) this.findViewById(R.id.etNewPwd);
        etConfirmNewPwd = (EditText) this.findViewById(R.id.etConfirmNewPwd);

        Intent intent = this.getIntent();
        sCode = intent.getStringExtra("Code");
        sPhoneNumber = intent.getStringExtra("PhoneNumber");
    }

    /**
     * 完成新密码的设置
     *
     * @param view
     */
    public void doFinishSetNewPwd(View view) {
        String sNewPwd = etNewPwd.getText().toString().trim();
        String sConfirmNewPwd = etConfirmNewPwd.getText().toString().trim();
        if (TextUtils.isEmpty(sNewPwd)) {
            CommonUtil.showTips(this, R.mipmap.warning, "请填写新密码");
            return;
        }
        if (sNewPwd.length()<8) {
            CommonUtil.showTips(this, R.mipmap.warning, "密码不能小于8位!");
            return;
        }
        if (TextUtils.isEmpty(sConfirmNewPwd)) {
            CommonUtil.showTips(this, R.mipmap.warning, "请确认新密码");
            return;
        }
        if (!sNewPwd.equals(sConfirmNewPwd)) {
            CommonUtil.showTips(this, R.mipmap.warning, "两次密码不一致，重新设置");
            return;
        }
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在设置，请稍后...");
        loadingDialog.show();
        if (!CommonUtil.isNetworkAvailable(this)) {
            CommonUtil.showTips(this, R.mipmap.warning, "网络不可用");
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            return;
        }
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String account = sCode + " " + sPhoneNumber;
        params.put("account", account);
        params.put("password", sNewPwd);
        params.put("sign", CommonUtil.md5Encryption(account + Constant.Token));
        httpClient.post(Constant.sResetUserPwdUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }

                    String sresponse = String.valueOf(response);
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(sresponse);
                    int code = jsonObject.getInteger("code");
                    String msg = jsonObject.getString("message");

                    if (statusCode == 200) {
                        if (code == 0) {
                            Intent intent = new Intent(SetNewPwdActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            CommonUtil.showTips(SetNewPwdActivity.this, R.mipmap.smile, msg);
                        } else {
                            CommonUtil.showTips(SetNewPwdActivity.this, R.mipmap.warning, msg);
                        }
                    } else {
                        CommonUtil.showTips(SetNewPwdActivity.this, R.mipmap.warning, "请求失败");
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
                CommonUtil.showTips(SetNewPwdActivity.this, R.mipmap.warning, "请求失败");
            }
        });

    }

}
