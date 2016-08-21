package com.livechat.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.livechat.chat.service.UserLoginInfoService;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.livechat.chat.widget.LoadingDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * 邮箱地址
 */
public class ChangeEmailAdrActivity extends BaseActivity {

    private EditText etEmail;

    private LoadingDialog loadingDialog;
    private UserLoginInfoService loginInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email_adr);

        initUI();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        loginInfoService = new UserLoginInfoService(this);
        etEmail = (EditText) this.findViewById(R.id.etEmail);

        String sEmailAdr = this.getIntent().getStringExtra("eMailAdr");
        etEmail.setText(sEmailAdr);
        etEmail.setSelection(sEmailAdr.length());
    }

    /**
     * 保存邮箱地址
     *
     * @param view
     */
    public void doSaveModify(View view) {
        final String sEmailAdr = etEmail.getText().toString().trim();
        if (!CommonUtil.isEmail(sEmailAdr)) {
            CommonUtil.showTips(this, R.mipmap.warning, "邮箱地址格式不正确");
            return;
        }
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在修改，请稍后...");
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
        final String account = App.getInstance().getsUserLoginId();
        params.put("account", account);
        params.put("sign", CommonUtil.md5Encryption(account + Constant.Token));// 签名(md5(account + token))
        params.put("email", sEmailAdr);
        httpClient.post(Constant.sUpdateUserInfoUrl, params, new JsonHttpResponseHandler() {
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
                            // 更新本地数据
                            loginInfoService.updateUsersEmail(sEmailAdr, account);
                            finish();
                            CommonUtil.showTips(ChangeEmailAdrActivity.this, R.mipmap.smile, msg);
                        } else {
                            CommonUtil.showTips(ChangeEmailAdrActivity.this, R.mipmap.warning, msg);
                        }
                    } else {
                        CommonUtil.showTips(ChangeEmailAdrActivity.this, R.mipmap.warning, "请求失败");
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
                CommonUtil.showTips(ChangeEmailAdrActivity.this, R.mipmap.warning, "请求失败");
            }
        });

    }

}
