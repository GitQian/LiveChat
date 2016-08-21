package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.livechat.chat.entity.UserLoginInfoBean;
import com.livechat.chat.service.UserLoginInfoService;
import com.livechat.chat.utils.BitmapCache;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;

/**
 * 个人信息
 */
public class PersonInfoActivity extends BaseActivity {

    private ImageView ivHead;
    private TextView tvUserName, tvUserID, tvUserSex, tvUserArea, tvUserSign;

    private UserLoginInfoService loginInfoService;
    private String sHeadPicUrl = "";
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        mQueue= Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());

        initUI();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        loginInfoService = new UserLoginInfoService(this);
        ivHead = (ImageView) this.findViewById(R.id.ivHead);
        tvUserName = (TextView) this.findViewById(R.id.tvUserName);
        tvUserID = (TextView) this.findViewById(R.id.tvUserID);
        tvUserSex = (TextView) this.findViewById(R.id.tvUserSex);
        tvUserArea = (TextView) this.findViewById(R.id.tvUserArea);
        tvUserSign = (TextView) this.findViewById(R.id.tvUserSign);
    }

    @Override
    protected void onResume() {
        // 从本地数据库获取
        UserLoginInfoBean userLoginInfoBean = loginInfoService.getSimpleUserLoginInfo(CommonUtil.getUserLoginInfo(this, Constant.ACCOUNT_CODE));
        if (userLoginInfoBean != null) {
            // 头像
            sHeadPicUrl = userLoginInfoBean.getsHeaderImage().equals("") ? "" : userLoginInfoBean.getsHeaderImage();
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(ivHead, 0, R.mipmap.head);
            mImageLoader.get(sHeadPicUrl, listener);
//            CommonUtil.showImageLoader(sHeadPicUrl, ivHead, R.mipmap.head);
            // 昵称
            tvUserName.setText(userLoginInfoBean.getsNickname().equals("") ? "未设置" : userLoginInfoBean.getsNickname());
            // 账号ID
            tvUserID.setText(userLoginInfoBean.getsAccount().equals("") ? "" : userLoginInfoBean.getsAccount());
            // 性别
            if (userLoginInfoBean.getiGender() == 0) {
                tvUserSex.setText("男");
            } else if (userLoginInfoBean.getiGender() == 1) {
                tvUserSex.setText("女");
            }
            // 地区
            String sAddress = userLoginInfoBean.getsAddress();
            if (sAddress.contains("中国")) {
                sAddress = sAddress.replace("中国 ", "");
                tvUserArea.setText(sAddress.equals("") ? "未设置" : sAddress);
            } else {
                sAddress = sAddress.replace(" ", "");
                tvUserArea.setText(sAddress.equals("") ? "未设置" : sAddress);
            }
            // 个性签名
            tvUserSign.setText(userLoginInfoBean.getsSignature().equals("") ? "未设置" : userLoginInfoBean.getsSignature());
        }
        super.onResume();
    }

    /**
     * 头像
     *
     * @param view
     */
    public void doRlHeadPic(View view) {
        Intent intent = new Intent(this, SetHeadPicActivity.class);
        intent.putExtra("sHeadPicUrl", sHeadPicUrl);
        startActivity(intent);
    }

    /**
     * 名字
     *
     * @param view
     */
    public void doRlName(View view) {
        Intent intent = new Intent(this, ModifyUserNameActivity.class);
        intent.putExtra("UserName", tvUserName.getText().toString().equals("未设置") ? "" : tvUserName.getText());
        startActivity(intent);
    }

    /**
     * 帐号ID
     *
     * @param view
     */
    public void doRlUserID(View view) {

    }

    /**
     * 我的地址
     *
     * @param view
     */
    public void doRlMyAdr(View view) {

    }

    /**
     * 性别
     *
     * @param view
     */
    public void doRlUserSex(View view) {
        Intent intent = new Intent(this, ChangeSexActivity.class);
        intent.putExtra("gender", tvUserSex.getText().toString());
        startActivity(intent);
    }

    /**
     * 地区
     *
     * @param view
     */
    public void doRlUserArea(View view) {
        Intent intent = new Intent(this, RegionAddrActivity.class);
        String[] addrs = tvUserArea.getText().toString().split(" ");
        if (addrs.length == 1) {// 国外的
            intent.putExtra("mCountryStr", addrs[0]);
            intent.putExtra("mProvinceStr", "");
            intent.putExtra("mCityStr", "");
        } else {// 国内的
            intent.putExtra("mCountryStr", "中国");
            intent.putExtra("mProvinceStr", addrs[0].replace("省", ""));
            intent.putExtra("mCityStr", addrs[1].replace("市", ""));
        }
        startActivity(intent);
    }

    /**
     * 个性签名
     *
     * @param view
     */
    public void doRlUserSign(View view) {
        Intent intent = new Intent(this, IndividualitySignatureActivity.class);
        intent.putExtra("initSign", tvUserSign.getText().toString().equals("未设置") ? "" : tvUserSign.getText());
        startActivity(intent);
    }

}
