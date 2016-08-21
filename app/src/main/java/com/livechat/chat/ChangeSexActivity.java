package com.livechat.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
 * 性别
 */
public class ChangeSexActivity extends BaseActivity {

    private ImageView ivMale, ivFeMale;

    private LoadingDialog loadingDialog;
    private UserLoginInfoService loginInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_sex);

        initUI();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        loginInfoService = new UserLoginInfoService(this);

        ivMale = (ImageView) this.findViewById(R.id.ivMale);
        ivFeMale = (ImageView) this.findViewById(R.id.ivFeMale);
    }

    @Override
    protected void onResume() {
        // 获取
        String gender = this.getIntent().getStringExtra("gender");
        if (gender.equals("男")) {
            ivMale.setVisibility(View.VISIBLE);
            ivFeMale.setVisibility(View.GONE);
        } else {
            ivMale.setVisibility(View.GONE);
            ivFeMale.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    /**
     * 男
     *
     * @param view
     */
    public void doRlMale(View view) {
        genderPostServer(0);
        ivMale.setVisibility(View.VISIBLE);
        ivFeMale.setVisibility(View.GONE);
    }

    /**
     * 女
     *
     * @param view
     */
    public void doRlFeMale(View view) {
        genderPostServer(1);
        ivMale.setVisibility(View.GONE);
        ivFeMale.setVisibility(View.VISIBLE);
    }

    /**
     * 将性别提交到服务器上去
     *
     * @param gender
     */
    private void genderPostServer(final int gender) {
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在操作，请稍后...");
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
        params.put("gender", gender);
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
                            loginInfoService.updateUseriGender(gender, account);
                            finish();
                            CommonUtil.showTips(ChangeSexActivity.this, R.mipmap.smile, msg);
                        } else {
                            CommonUtil.showTips(ChangeSexActivity.this, R.mipmap.warning, msg);
                        }
                    } else {
                        CommonUtil.showTips(ChangeSexActivity.this, R.mipmap.warning, "请求超时");
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
                CommonUtil.showTips(ChangeSexActivity.this, R.mipmap.warning, "请求超时");
            }
        });
    }

}
