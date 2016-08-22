package com.livechat.chat.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.livechat.chat.BaseActivity;
import com.livechat.chat.LoginActivity;
import com.livechat.chat.R;
import com.livechat.chat.adapter.FragmentAdapter;
import com.livechat.chat.utils.CommonUtil;

import io.rong.imlib.RongIMClient;

/**
 * 主界面
 */
public class LiveChatMainActivity extends BaseActivity implements View.OnClickListener {

    public static final int TAB_PAGE1 = 0;
    public static final int TAB_PAGE2 = 1;
    public static final int TAB_PAGE3 = 2;
    public static final int TAB_PAGE4 = 3;

    private ViewPager viewPager;
    private RelativeLayout rlCustomerSc, rlInfoAct, rlMe, rlOther;
    private ImageView ivCustomerSc, ivInfoAct, ivMe, ivOther;
    private TextView tvCustomerSc, tvInfoAct, tvMe, tvOther;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat_main);

        initView();
        mHandler  = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0){
//                    Toast.makeText(LiveChatMainActivity.this, "帐号在其他设备上登录！", Toast.LENGTH_LONG).show();
                    new AlertDialog.Builder(LiveChatMainActivity.this).setCancelable(false).setTitle("登陆异常").setMessage("您的账号在其他设备登陆").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CommonUtil.saveUserLoginInfo(LiveChatMainActivity.this,"", "", "0", "");
                            Intent intent = new Intent(LiveChatMainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CommonUtil.saveUserLoginInfo(LiveChatMainActivity.this,"", "", "0", "");
                            Intent intent = new Intent(LiveChatMainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).show();
                }
                return false;
            }
        });
        RongIMClient.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                Log.d("qiansheng", "" + connectionStatus.getMessage().toString());
                if ("Login on the other device, and be kicked offline.".equals(connectionStatus.getMessage())) {
                    mHandler.sendEmptyMessage(0);
                }else {

                }
            }
        });
        addListener();
    }
    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.activity_tab_pay_close, R.anim.activity_tab_pay_close);
    }
    /**
     * 初始化视图控件UI
     */
    private void initView() {
        viewPager = (ViewPager) this.findViewById(R.id.viewPager);
        rlCustomerSc = (RelativeLayout) this.findViewById(R.id.rlCustomerSc);
        rlInfoAct = (RelativeLayout) this.findViewById(R.id.rlInfoAct);
        rlMe = (RelativeLayout) this.findViewById(R.id.rlMe);
        rlOther = (RelativeLayout) this.findViewById(R.id.rlOther);

        ivCustomerSc = (ImageView) this.findViewById(R.id.ivCustomerSc);
        ivInfoAct = (ImageView) this.findViewById(R.id.ivInfoAct);
        ivMe = (ImageView) this.findViewById(R.id.ivMe);
        ivOther = (ImageView) this.findViewById(R.id.ivOther);

        tvCustomerSc = (TextView) this.findViewById(R.id.tvCustomerSc);
        tvInfoAct = (TextView) this.findViewById(R.id.tvInfoAct);
        tvMe = (TextView) this.findViewById(R.id.tvMe);
        tvOther = (TextView) this.findViewById(R.id.tvOther);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    /**
     * 添加事件
     */
    private void addListener() {
        rlCustomerSc.setOnClickListener(this);
        rlInfoAct.setOnClickListener(this);
        rlMe.setOnClickListener(this);
        rlOther.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case TAB_PAGE1:
                        ivCustomerSc.setImageResource(R.mipmap.customer_sc_pressed);
                        ivInfoAct.setImageResource(R.mipmap.info_act_normal2);
                        ivMe.setImageResource(R.mipmap.me_normal);
                        ivOther.setImageResource(R.mipmap.e8_normal);

                        tvCustomerSc.setTextColor(Color.parseColor("#FF1552"));
                        tvInfoAct.setTextColor(Color.parseColor("#FFFFFF"));
                        tvMe.setTextColor(Color.parseColor("#FFFFFF"));
                        tvOther.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case TAB_PAGE2:
                        ivCustomerSc.setImageResource(R.mipmap.customer_sc_normal);
                        ivInfoAct.setImageResource(R.mipmap.info_act_pressed2);
                        ivMe.setImageResource(R.mipmap.me_normal);
                        ivOther.setImageResource(R.mipmap.e8_normal);

                        tvCustomerSc.setTextColor(Color.parseColor("#FFFFFF"));
                        tvInfoAct.setTextColor(Color.parseColor("#FF1552"));
                        tvMe.setTextColor(Color.parseColor("#FFFFFF"));
                        tvOther.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case TAB_PAGE3:
                        ivCustomerSc.setImageResource(R.mipmap.customer_sc_normal);
                        ivInfoAct.setImageResource(R.mipmap.info_act_normal2);
                        ivMe.setImageResource(R.mipmap.me_pressed);
                        ivOther.setImageResource(R.mipmap.e8_normal);

                        tvCustomerSc.setTextColor(Color.parseColor("#FFFFFF"));
                        tvInfoAct.setTextColor(Color.parseColor("#FFFFFF"));
                        tvMe.setTextColor(Color.parseColor("#FF1552"));
                        tvOther.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case TAB_PAGE4:
                        ivCustomerSc.setImageResource(R.mipmap.customer_sc_normal);
                        ivInfoAct.setImageResource(R.mipmap.info_act_normal2);
                        ivMe.setImageResource(R.mipmap.me_normal);
                        ivOther.setImageResource(R.mipmap.e8);

                        tvCustomerSc.setTextColor(Color.parseColor("#FFFFFF"));
                        tvInfoAct.setTextColor(Color.parseColor("#FFFFFF"));
                        tvMe.setTextColor(Color.parseColor("#FFFFFF"));
                        tvOther.setTextColor(Color.parseColor("#FF1552"));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlCustomerSc:
                viewPager.setCurrentItem(TAB_PAGE1);
                break;
            case R.id.rlInfoAct:
                viewPager.setCurrentItem(TAB_PAGE2);
                break;
            case R.id.rlMe:
                viewPager.setCurrentItem(TAB_PAGE3);
                break;
            case R.id.rlOther:
                viewPager.setCurrentItem(TAB_PAGE4);
                break;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("\n确定退出吗？\n").setPositiveButton("退出", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    if (RongIM.getInstance() != null) {
//                        RongIM.getInstance().disconnect(true);
//                    }
//                    // android.os.Process.killProcess(android.os.Process.myPid());
//                    CommonUtil.saveUserLoginInfo(LiveChatMainActivity.this, "", "", "0", "LiveChat1");
//                    finish();
//                }
//            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            }).show();
//        }
//        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()){
//            CommonUtil.saveUserLoginInfo(LiveChatMainActivity.this, "", "", "0", "LiveChat1");
//            finish();
//        }
//        return false;
//    }

}
