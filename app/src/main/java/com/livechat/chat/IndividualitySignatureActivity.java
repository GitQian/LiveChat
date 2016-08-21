package com.livechat.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * 个性签名
 */
public class IndividualitySignatureActivity extends BaseActivity {

    private EditText etSignContent;
    private TextView tvNum;

    private String initSign = "";
    private LoadingDialog loadingDialog;
    private UserLoginInfoService loginInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individuality_signature);

        initUI();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        loginInfoService = new UserLoginInfoService(this);

        initSign = this.getIntent().getStringExtra("initSign");
        etSignContent = (EditText) this.findViewById(R.id.etSignContent);
        tvNum = (TextView) this.findViewById(R.id.tvNum);
        etSignContent.setText(initSign);
        etSignContent.setSelection(initSign.length());

        tvNum.setText((30 - (initSign.length())) + "");
        editSignatureChange();
    }

    /**
     * 保存修改
     *
     * @param view
     */
    public void doSaveModify(View view) {
        final String signContent = etSignContent.getText().toString().trim();
        // 提交数据到服务器
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在保存...");
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
        params.put("signature", signContent);
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
                            loginInfoService.updateUsersSignature(signContent, account);
                            finish();
                            CommonUtil.showTips(IndividualitySignatureActivity.this, R.mipmap.smile, msg);
                        } else {
                            CommonUtil.showTips(IndividualitySignatureActivity.this, R.mipmap.warning, msg);
                        }
                    } else {
                        CommonUtil.showTips(IndividualitySignatureActivity.this, R.mipmap.warning, "请求失败");
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
                CommonUtil.showTips(IndividualitySignatureActivity.this, R.mipmap.warning, "请求失败");
            }
        });
    }

    /**
     * 剩下多少字的变化
     */
    private void editSignatureChange() {
        etSignContent.addTextChangedListener(new TextWatcher() {

            private CharSequence cTemp;
            private int iSelectionStart;
            private int iSelectionEnd;
            private int iTotalnum = 30;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cTemp = s;
                if (s.length() == 30) {
                    Toast.makeText(IndividualitySignatureActivity.this, "已达最大字数上限", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = iTotalnum - s.length();
                tvNum.setText(number + "");
                iSelectionStart = etSignContent.getSelectionStart();
                iSelectionEnd = etSignContent.getSelectionEnd();
                if (cTemp.length() > iTotalnum) {
                    s.delete(iSelectionStart - 1, iSelectionEnd);
                    etSignContent.setText(s);
                    etSignContent.setSelection(s.length());// 设置光标在最后
                }
            }
        });
    }

}
