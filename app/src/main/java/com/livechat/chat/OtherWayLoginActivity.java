package com.livechat.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.livechat.chat.entity.UserLoginInfoBean;
import com.livechat.chat.fragment.LiveChatMainActivity;
import com.livechat.chat.service.UserLoginInfoService;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.livechat.chat.widget.LoadingDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 其他方式登陆
 */
public class OtherWayLoginActivity extends BaseActivity {

    private EditText etID, etPwd;

    private LoadingDialog loadingDialog;
    private UserLoginInfoService loginInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_way_login);

        initUI();
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
        etID = (EditText) this.findViewById(R.id.etID);
        etPwd = (EditText) this.findViewById(R.id.etPwd);

        loginInfoService = new UserLoginInfoService(this);
    }

    /**
     * 登陆
     *
     * @param view
     */
    public void doBtnLogin(View view) {
        final String sId = etID.getText().toString().trim();
        final String sPwd = etPwd.getText().toString().trim();

        if (TextUtils.isEmpty(sId)) {
            CommonUtil.showTips(this, R.mipmap.warning, "ID号不能为空");
            return;
        }
        if (TextUtils.isEmpty(sPwd)) {
            CommonUtil.showTips(this, R.mipmap.warning, "密码不能为空");
            return;
        }
        if (sPwd.length()<8) {
            CommonUtil.showTips(this, R.mipmap.warning, "密码不能小于8位数!");
            return;
        }
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在登录...");
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
        params.put("account", sId);// 用户登录的账号
        params.put("password", sPwd);// 密码
        params.put("channelId", CommonUtil.getUserLoginInfo(getApplicationContext(), Constant.ChannelId_Code));// 百度推送的ID
        params.put("appType", 3);// 应用类型[5.web;4.iOS;3.Android;]
        params.put("sign", CommonUtil.md5Encryption("0" + Constant.Token));// 签名(md5("0" + token))
        httpClient.post(Constant.sLoginUrl, params, new JsonHttpResponseHandler() {

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
                    com.alibaba.fastjson.JSONObject data = (com.alibaba.fastjson.JSONObject) jsonObject.get("data");

                    if (statusCode == 200) {
                        if (code == 9007) {// 密码错误
                            CommonUtil.showTips(OtherWayLoginActivity.this, R.mipmap.warning, msg);
                        } else if (code == 9003) {// 账号或密码错误
                            CommonUtil.showTips(OtherWayLoginActivity.this, R.mipmap.warning, msg);
                        } else if (code == 9002) {// 用户不存在
                            CommonUtil.showTips(OtherWayLoginActivity.this, R.mipmap.warning, msg);
                        } else if (code == 9001) {// 签名错误
                            CommonUtil.showTips(OtherWayLoginActivity.this, R.mipmap.warning, msg);
                        } else if (code == 0) {// 登录成功
                            if (data != null && data.size() > 0) {
                                String sUserId = data.getString("userId") == null ? "" : data.getString("userId");
                                String sAccount = data.getString("account") == null ? "" : data.getString("account");
                                String sPassword = data.getString("password") == null ? "" : data.getString("password");
                                String sNickname = data.getString("nickname") == null ? "" : data.getString("nickname");
                                String sBirthday = data.getString("birthday") == null ? "" : data.getString("birthday");
                                int iGender = data.getInteger("gender") == null ? 0 : data.getInteger("gender");
                                String sAge = data.getString("age") == null ? "" : data.getString("age");
                                String sHeaderImage = data.getString("headerImage") == null ? "" : data.getString("headerImage");
                                String sEmail = data.getString("email") == null ? "" : data.getString("email");
                                String sPhone = data.getString("phone") == null ? "" : data.getString("phone");
                                String sAddress = data.getString("address") == null ? "" : data.getString("address");
                                String signature = data.getString("signature") == null ? "" : data.getString("signature");
                                String sRongToken = data.getString("rongToken") == null ? "" : data.getString("rongToken");
                                int iStatus = data.getInteger("status") == null ? 0 : data.getInteger("status");
                                String sIp = data.getString("ip") == null ? "" : data.getString("ip");
                                String sCreateDay = data.getString("createDay") == null ? "" : data.getString("createDay");
                                int iAppType = data.getInteger("appType") == null ? 0 : data.getInteger("appType");
                                int iOnlineStatus = data.getInteger("onlineStatus") == null ? 0 : data.getInteger("onlineStatus");
                                String sLastLogin = data.getString("lastLogin") == null ? "" : data.getString("lastLogin");

                                UserLoginInfoBean userLoginInfoBean = new UserLoginInfoBean();
                                userLoginInfoBean.setsUserId(sUserId);
                                userLoginInfoBean.setsAccount(sAccount);
                                userLoginInfoBean.setsPassword(sPassword);
                                userLoginInfoBean.setsNickname(sNickname);
                                userLoginInfoBean.setsBirthday(sBirthday);
                                userLoginInfoBean.setiGender(iGender);
                                userLoginInfoBean.setsAge(sAge);
                                userLoginInfoBean.setsHeaderImage(sHeaderImage);
                                userLoginInfoBean.setsEmail(sEmail);
                                userLoginInfoBean.setsPhone(sPhone);
                                userLoginInfoBean.setsAddress(sAddress);
                                userLoginInfoBean.setsSignature(signature);
                                userLoginInfoBean.setsRongToken(sRongToken);
                                userLoginInfoBean.setiStatus(iStatus);
                                userLoginInfoBean.setsIp(sIp);
                                userLoginInfoBean.setsCreateDay(sCreateDay);
                                userLoginInfoBean.setiAppType(iAppType);
                                userLoginInfoBean.setiOnlineStatus(iOnlineStatus);
                                userLoginInfoBean.setsLastLogin(sLastLogin);

                                // 设置用户的唯一性, 作为用户数据库的命名
                                App.getInstance().setsUserLoginId(sAccount);
                                // 保存一些用户登录的信息
                                CommonUtil.saveUserLoginInfo(OtherWayLoginActivity.this, sPhone, sPassword, "1", "LiveChat2");
                                // 判断用户的账号是否违法
                                if (iStatus == 0) {// 账号被封住(1:封住;0:正常)
                                    conn(sRongToken);// 连接融云服务器
                                } else {
                                    CommonUtil.showTips(OtherWayLoginActivity.this, R.mipmap.warning, "登陆失败，您账号被封了");
                                    return;
                                }
                                // 添加用户数据或者更新用户数据
                                if (loginInfoService.isExists(sUserId) > 0) {// 存在记录就更新
                                    loginInfoService.updateUserInfo(userLoginInfoBean);
                                } else {// 不存在记录就添加
                                    loginInfoService.insertUserInfo(userLoginInfoBean);
                                }
                            }
                        }
                    } else {
                        CommonUtil.showTips(OtherWayLoginActivity.this, R.mipmap.warning, "请求失败");
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
                CommonUtil.showTips(OtherWayLoginActivity.this, R.mipmap.warning, "请求失败");
            }
        });
    }

    /**
     * 忘记密码
     *
     * @param view
     */
    public void doForgetPwd(View view) {
        Intent intent = new Intent(this, ForgetPwdActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open, 0);
    }

    /**
     * 注册账号
     *
     * @param view
     */
    public void doRegisterAccount(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open, 0);
    }

    /**
     * 问题
     *
     * @param view
     */
    public void doQuestion(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open, 0);
    }

    /**
     * 其他方式登陆
     *
     * @param view
     */
    public void doOtherWayLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open, 0);
    }

    /**
     * 连接融云
     */
    private void conn(final String Token) {
        if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(Token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userId 当前 token
                 */
                @Override
                public void onSuccess(String userId) {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    Log.d("LoginActivity", "--onSuccess:" + userId);
                    Intent intent = new Intent(OtherWayLoginActivity.this, LiveChatMainActivity.class);// LoggedInActivity
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_open, 0);
                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    Log.d("LoginActivity", "--onError：" + errorCode.getValue());
                    CommonUtil.showTips(OtherWayLoginActivity.this, R.mipmap.warning, "连接服务器失败，请稍后连接...");
                }
            });
        }
    }

    /**
     * 关闭键盘
     *
     * @param view
     */
    public void doALayout(View view) {
        View view1 = getWindow().peekDecorView();
        if (view1 != null) {
            InputMethodManager inputManger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
