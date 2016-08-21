package com.livechat.chat;

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
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity {

    private EditText etOldPwd, etNewPwd;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initUI();
    }

    /**
     * 初始化界面UI
     */
    private void initUI() {
        etOldPwd = (EditText) this.findViewById(R.id.etOldPwd);
        etNewPwd = (EditText) this.findViewById(R.id.etNewPwd);
    }

    /**
     * 修改
     *
     * @param view
     */
    public void doChangePwd(View view) {
        String oldPwd = CommonUtil.getUserLoginInfo(this, Constant.PASSWORD_CODE);
        String sOldPwd = etOldPwd.getText().toString().trim();
        String sNewPwd = etNewPwd.getText().toString().trim();
        if (TextUtils.isEmpty(sOldPwd)) {
            CommonUtil.showTips(this, R.mipmap.warning, "请填写旧密码");
            return;
        }
        if (!oldPwd.equals(sOldPwd)) {
            CommonUtil.showTips(this, R.mipmap.warning, "旧密码错误，重新填写");
            return;
        }
        if (TextUtils.isEmpty(sNewPwd)) {
            CommonUtil.showTips(this, R.mipmap.warning, "请填写新密码");
            return;
        }
        if (!CommonUtil.isVerification(sNewPwd, "password")) {
            CommonUtil.showTips(this, R.mipmap.warning, "密码有非法字符，重新设置");
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
        String account = CommonUtil.getUserLoginInfo(this, Constant.ACCOUNT_CODE);
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
                    String msg = jsonObject.getString("message");
                    int code = jsonObject.getInteger("code");

                    if (statusCode == 200) {
                        if (code == 0) {
                            finish();
                            CommonUtil.showTips(ChangePasswordActivity.this, R.mipmap.smile, msg);
                        } else {
                            CommonUtil.showTips(ChangePasswordActivity.this, R.mipmap.warning, msg);
                        }
                    } else {
                        CommonUtil.showTips(ChangePasswordActivity.this, R.mipmap.warning, "请求失败");
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
                CommonUtil.showTips(ChangePasswordActivity.this, R.mipmap.warning, "请求失败");
            }
        });

    }

}
