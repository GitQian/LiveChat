package com.livechat.chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.livechat.chat.AboutUsActivity;
import com.livechat.chat.FeedBackThreeActivity;
import com.livechat.chat.FeedBackTwoActivity;
import com.livechat.chat.PersonInfoActivity;
import com.livechat.chat.R;
import com.livechat.chat.SettingActivity;
import com.livechat.chat.UpdateVersion;
import com.livechat.chat.entity.UserLoginInfoBean;
import com.livechat.chat.service.UserLoginInfoService;
import com.livechat.chat.utils.BitmapCache;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.livechat.chat.widget.RoundedRectImageView;

/**
 * 我
 * Created by Administrator on 2016/3/21.
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private RoundedRectImageView rvHeadPic;
    private TextView tvUserName, tvUserID;

    private String sPhone = "", sEmail = "";
    private UserLoginInfoService loginInfoService;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_layout, container, false);
        mQueue= Volley.newRequestQueue(getActivity());
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());

        rvHeadPic = (RoundedRectImageView) view.findViewById(R.id.rvHeadPic);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvUserID = (TextView) view.findViewById(R.id.tvUserID);
        loginInfoService = new UserLoginInfoService(getActivity());

        (view.findViewById(R.id.rlPersonInfo)).setOnClickListener(this);
        (view.findViewById(R.id.rlSetting)).setOnClickListener(this);
        (view.findViewById(R.id.rlAboutUs)).setOnClickListener(this);
        (view.findViewById(R.id.my_upgrade)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateVersion(getActivity()).showNoticeDialog();
            }
        });
        (view.findViewById(R.id.rlSharePolite)).setOnClickListener(this);
        (view.findViewById(R.id.rlFeedBack)).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rlPersonInfo:// 个人信息
                intent.setClass(getActivity(), PersonInfoActivity.class);
                break;
            case R.id.rlSetting:// 设置
                intent.setClass(getActivity(), SettingActivity.class);
                break;
            case R.id.rlAboutUs:// 关于我们
                intent.setClass(getActivity(), AboutUsActivity.class);// AboutActivity
                break;
//            case R.id.my_upgrade:// 本版更新
//                CommonUtil.showTips(getActivity(), R.mipmap.warning, "本版更新");
//                new UpdateVersion(getActivity()).checkUpdateInfo();
//                break;
            case R.id.rlSharePolite:// 分享有礼
                intent.setClass(getActivity(), FeedBackTwoActivity.class);
                break;
            case R.id.rlFeedBack:// 意见反馈
                intent.setClass(getActivity(), FeedBackThreeActivity.class);// HelpFeedBackActivity
                intent.putExtra("Phone", sPhone);
                intent.putExtra("Email", sEmail);
                break;
        }
        startActivity(intent);
    }

    /**
     * 获取初始化数据信息
     */
    @Override
    public void onResume() {
        // 从本地数据库获取
        UserLoginInfoBean userLoginInfoBean = loginInfoService.getSimpleUserLoginInfo(CommonUtil.getUserLoginInfo(getActivity(), Constant.ACCOUNT_CODE));
        if (userLoginInfoBean != null) {
            // 头像
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(rvHeadPic, 0, R.mipmap.head);
            mImageLoader.get(userLoginInfoBean.getsHeaderImage().equals("") ? "" : userLoginInfoBean.getsHeaderImage(), listener);
//            CommonUtil.showImageLoader(userLoginInfoBean.getsHeaderImage().equals("") ? "" : userLoginInfoBean.getsHeaderImage(), rvHeadPic, R.mipmap.head);
            // 昵称
            tvUserName.setText(userLoginInfoBean.getsNickname().equals("") ? "未设置" : userLoginInfoBean.getsNickname());
            // 账号ID
            tvUserID.setText(userLoginInfoBean.getsAccount().equals("") ? "账号ID：0" : "账号ID：" + userLoginInfoBean.getsAccount());
            // 手机号码
            sPhone = userLoginInfoBean.getsPhone().equals("") ? "" : userLoginInfoBean.getsPhone();
            // 电子邮件
            sEmail = userLoginInfoBean.getsEmail().equals("") ? "" : userLoginInfoBean.getsEmail();
        }
        super.onResume();
    }

}
