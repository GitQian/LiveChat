package com.livechat.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.livechat.chat.entity.UserLoginInfoBean;
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

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {

    private LoadingDialog loadingDialog;
    private UserLoginInfoService userLoginInfoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        userLoginInfoService = new UserLoginInfoService(this);
    }

    /**
     * 帐号与安全
     *
     * @param view
     */
    public void doRlAccount(View view) {
        Intent intent = new Intent(this, AccountSecurityActivity.class);
        startActivity(intent);
    }

    /**
     * 新消息通知
     *
     * @param view
     */
    public void doRlNewNotify(View view) {
        Intent intent = new Intent(this, NewsNotifyActivity.class);
        startActivity(intent);
    }

    /**
     * 隐私
     *
     * @param view
     */
    public void doRlPrivacy(View view) {

    }

    /**
     * 通用
     *
     * @param view
     */
    public void doRlGeneral(View view) {
        Intent intent = new Intent(this, GeneralActivity.class);
        startActivity(intent);
    }

    /**
     * 帮助与反馈
     *
     * @param view
     */
    public void doRlHelpFeedBack(View view) {
        Intent intent = new Intent(this, HelpAndFeedbackActivity.class);// HelpFeedBackActivity
        startActivity(intent);
    }

    /**
     * 关于LiveChat
     *
     * @param view
     */
    public void doRlAboutLiveChat(View view) {

    }

    /**
     * 退出登录
     *
     * @param view
     */
    public void doBtnExitLogin(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("\n您确定退出吗？\n").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                loadingDialog = new LoadingDialog(SettingActivity.this, Constant.THEME_HOLO_LIGHT, "正在退出...");
                loadingDialog.show();
                if (!CommonUtil.isNetworkAvailable(SettingActivity.this)) {
                    CommonUtil.showTips(SettingActivity.this, R.mipmap.warning, "网络不可用");
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    return;
                }
                AsyncHttpClient httpClient = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                String account = App.getInstance().getsUserLoginId();
                params.put("account", account);
                UserLoginInfoBean loginInfoBean = userLoginInfoService.getSimpleUserLoginInfoBysAccount(account);
                params.put("password", loginInfoBean.getsPassword());
                params.put("appType", 3);
                params.put("sign", CommonUtil.md5Encryption(account + Constant.Token));
                httpClient.post(Constant.sLogOutUrl, params, new JsonHttpResponseHandler() {

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
                                    // 断开融云服务器
                                    if (RongIM.getInstance() != null) {
                                        RongIM.getInstance().logout();
                                        RongIM.getInstance().disconnect(true);
                                    }
                                    Intent intent = new Intent();
                                    // 默认登陆方式
                                    if ((CommonUtil.getUserLoginInfo(SettingActivity.this, Constant.OnWays_Code)).equals("LiveChat1")) {
                                        // 将保存的手机和密码以及登陆状态都设置为字符串或0.
                                        CommonUtil.saveUserLoginInfo(SettingActivity.this, "", "", "0", "LiveChat1");
                                        intent.setClass(SettingActivity.this, LoginActivity.class);
                                    } else {
                                        // 将保存的手机和密码以及登陆状态都设置为字符串或0.
                                        CommonUtil.saveUserLoginInfo(SettingActivity.this, "", "", "0", "LiveChat2");
                                        intent.setClass(SettingActivity.this, OtherWayLoginActivity.class);
                                    }
                                    startActivity(intent);
                                    SettingActivity.this.finish();
                                    ActivityCollector.finishAllActivity();
                                    CommonUtil.showTips(SettingActivity.this, R.mipmap.smile, msg);
                                } else {
                                    CommonUtil.showTips(SettingActivity.this, R.mipmap.warning, msg);
                                }
                            } else {
                                CommonUtil.showTips(SettingActivity.this, R.mipmap.warning, "请求失败");
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
                        CommonUtil.showTips(SettingActivity.this, R.mipmap.warning, "请求失败");
                    }
                });


            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

}
