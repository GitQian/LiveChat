package com.livechat.chat;

import android.os.Bundle;
import android.text.TextUtils;
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
 * 修改用户名
 */
public class ModifyUserNameActivity extends BaseActivity {

    private EditText etUserName;

    private String userName = "";
    private LoadingDialog loadingDialog;
    private UserLoginInfoService loginInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_name);

        userName = this.getIntent().getStringExtra("UserName");
        etUserName = (EditText) this.findViewById(R.id.etUserName);
        etUserName.setText(userName);
        etUserName.setSelection(userName.length());

        loginInfoService = new UserLoginInfoService(this);
    }

    /**
     * 保存修改
     *
     * @param view
     */
    public void doSaveModify(View view) {
        final String sUserName = etUserName.getText().toString().trim();
        if (TextUtils.isEmpty(sUserName)) {
            CommonUtil.showTips(this, R.mipmap.warning, "请输入您的名字");
            return;
        }
        if (userName.equals(sUserName)) {
            CommonUtil.showTips(this, R.mipmap.warning, "名字相同，不用修改");
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
        params.put("nickname", sUserName);
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
                            // 更新本地数据库
                            loginInfoService.updateUsersNickname(sUserName, account);
                            finish();
                            CommonUtil.showTips(ModifyUserNameActivity.this, R.mipmap.smile, msg);
                        } else {
                            CommonUtil.showTips(ModifyUserNameActivity.this, R.mipmap.warning, msg);
                        }
                    } else {
                        CommonUtil.showTips(ModifyUserNameActivity.this, R.mipmap.warning, "请求失败");
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
                CommonUtil.showTips(ModifyUserNameActivity.this, R.mipmap.warning, "请求失败");
            }
        });
    }

}
