package com.livechat.chat;

import android.os.Bundle;
import android.view.View;

/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        addClickListener();
    }

    /**
     * 添加单击事件
     */
    private void addClickListener() {
        (this.findViewById(R.id.ivMore)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuestItem1)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuestItem2)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuestItem3)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuestItem4)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuestItem5)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuestItem6)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuestItem7)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuestItem8)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuestItem9)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuestItem10)).setOnClickListener(this);
        (this.findViewById(R.id.rlAllQuestion)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuickHelp)).setOnClickListener(this);
        (this.findViewById(R.id.rlFeedBack)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMore:// 更多

                break;
            case R.id.rlQuestItem1:// 闪退

                break;
            case R.id.rlQuestItem2:// 聊天

                break;
            case R.id.rlQuestItem3:// 客服中心

                break;
            case R.id.rlQuestItem4:// 通讯录

                break;
            case R.id.rlQuestItem5:// 朋友圈

                break;
            case R.id.rlQuestItem6:// 小视频

                break;
            case R.id.rlQuestItem7:// 视频或语音聊天

                break;
            case R.id.rlQuestItem8:// 打开或后台消息

                break;
            case R.id.rlQuestItem9:// 红包

                break;
            case R.id.rlQuestItem10:// 其他

                break;
            case R.id.rlAllQuestion:// 全部问题

                break;
            case R.id.rlQuickHelp:// 快捷帮助

                break;
            case R.id.rlFeedBack:// 意见反馈

                break;
        }
    }

}
