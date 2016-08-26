package com.livechat.chat;

import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.livechat.chat.entity.CustomerBean;
import com.livechat.chat.service.CustomerInfoService;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.livechat.chat.widget.LoadingDialog;
import com.livechat.chat.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 新消息通知
 */
public class NewsNotifyActivity extends BaseActivity implements View.OnClickListener {

    private ToggleButton toggleBtn1, toggleBtn2, toggleBtn3;
    //    private TextView tvCache;
    private LoadingDialog loadingDialog;
    private PopupWindow mPopWindow;

    private CustomerInfoService customerInfoService;
    private List<CustomerBean> customerBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_notify);

        customerInfoService = new CustomerInfoService(this);
        customerBeanList = new ArrayList<>();

        initUI();
        addOnToggleChanged();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        toggleBtn1 = (ToggleButton) this.findViewById(R.id.toggleBtn1);
        toggleBtn2 = (ToggleButton) this.findViewById(R.id.toggleBtn2);
        toggleBtn3 = (ToggleButton) this.findViewById(R.id.toggleBtn3);
//        tvCache = (TextView) this.findViewById(R.id.tvCache);
    }

    /**
     * 添加Toggle改变时的事件
     */
    private void addOnToggleChanged() {
        // toggle1
        toggleBtn1.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
//                Toast.makeText(getApplicationContext(), on + "", Toast.LENGTH_SHORT).show();
            }
        });
        // toggle2
        toggleBtn2.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
//                Toast.makeText(getApplicationContext(), on + "", Toast.LENGTH_SHORT).show();
            }
        });
        // toggle3
        toggleBtn3.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
//                Toast.makeText(getApplicationContext(), on + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 清理缓存
     *
     * @param view
     */
    public void doRlCleanCaChe(View view) {
//        tvCache.setText("0      ");
//        CommonUtil.showTips(this, R.mipmap.smile, "清理完毕，真爽");
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在清理...");
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                aa.sendEmptyMessage(1);
            }
        }).start();
    }

    public android.os.Handler aa = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadingDialog.dismiss();
            CommonUtil.showTips(getApplicationContext(), R.mipmap.smile, "清理完毕，真爽");
        }
    };

    /**
     * 删除聊天记录
     *
     * @param view
     */
    public void doClearChatRecorder(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_item, null);
        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        LinearLayout root = (LinearLayout) contentView.findViewById(R.id.root);
        TextView mClearChat = (TextView) contentView.findViewById(R.id.mClearChat);
        TextView mCancle = (TextView) contentView.findViewById(R.id.mCancle);
        root.setOnClickListener(this);
        mClearChat.setOnClickListener(this);
        mCancle.setOnClickListener(this);

        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_news_notify, null);
        mPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.root:
                mPopWindow.dismiss();
                break;
            case R.id.mClearChat:
                customerBeanList = customerInfoService.getListCustomerInfo("");
                for (CustomerBean customerBean : customerBeanList) {
                    customerBean.getsRongToken();
                    RongIM.getInstance().getRongIMClient().clearMessages(Conversation.ConversationType.PRIVATE, customerBean.getsAccount(), new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            CommonUtil.showTips(getApplicationContext(), R.mipmap.smile, "清理完毕");
                            mPopWindow.dismiss();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            CommonUtil.showTips(getApplicationContext(), R.mipmap.warning, "清理失败");
                            mPopWindow.dismiss();
                        }
                    });
                }
//                CommonUtil.showTips(getApplicationContext(), R.mipmap.smile, "清理完毕");
//                mPopWindow.dismiss();
                break;
            case R.id.mCancle:
                mPopWindow.dismiss();
                break;
        }
    }

}
