package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.livechat.chat.entity.UserLoginInfoBean;
import com.livechat.chat.service.UserLoginInfoService;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;

import org.xutils.x;

/**
 * 账号与安全
 */
public class AccountSecurityActivity extends BaseActivity {

    private UserLoginInfoService loginInfoService;

    private TextView tvUserName, tvMobilePhone, tvEmailAdr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);
        x.view().inject(this);
        initUI();
    }

    /**
     * 初始化界面UI
     */
    private void initUI() {
        loginInfoService = new UserLoginInfoService(this);
        tvUserName = (TextView) this.findViewById(R.id.tvUserName);
        tvMobilePhone = (TextView) this.findViewById(R.id.tvMobilePhone);
        tvEmailAdr = (TextView) this.findViewById(R.id.tvEmailAdr);
    }

    /**
     * 手机号
     *
     * @param view
     */
    public void doRlMobilePhone(View view) {

    }

    /**
     * 邮箱地址
     *
     * @param view
     */
    public void doRlEmailAdr(View view) {
        Intent intent = new Intent(this, ChangeEmailAdrActivity.class);
        intent.putExtra("eMailAdr", (tvEmailAdr.getText().toString()).equals("未填写") ? "" : tvEmailAdr.getText());
        startActivity(intent);
    }

    /**
     * 修改密码
     *
     * @param view
     */
    public void doRlModifyPwd(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    /**
     * 用户信息设置
     */
    @Override
    protected void onResume() {
        UserLoginInfoBean userLoginInfoBean = loginInfoService.getSimpleUserLoginInfo(CommonUtil.getUserLoginInfo(this, Constant.ACCOUNT_CODE));
        if (userLoginInfoBean != null) {
            // 账号
            tvUserName.setText(userLoginInfoBean.getsAccount());
            // 手机号
            tvMobilePhone.setText("+" + userLoginInfoBean.getsPhone());
            // 邮箱地址
            tvEmailAdr.setText(userLoginInfoBean.getsEmail().equals("") ? "未填写" : userLoginInfoBean.getsEmail());
        }
        super.onResume();
    }

}
