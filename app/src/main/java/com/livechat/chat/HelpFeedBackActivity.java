package com.livechat.chat;

import android.os.Bundle;
import android.view.View;

/**
 * 帮助与反馈
 */
public class HelpFeedBackActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_feed_back);

        initUI();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        (this.findViewById(R.id.rlItem2)).setOnClickListener(this);
        (this.findViewById(R.id.rlItem3)).setOnClickListener(this);
        (this.findViewById(R.id.rlItem4)).setOnClickListener(this);
        (this.findViewById(R.id.rlItem5)).setOnClickListener(this);
        (this.findViewById(R.id.rlAllQuestion)).setOnClickListener(this);
        (this.findViewById(R.id.rlQuickHelp)).setOnClickListener(this);
        (this.findViewById(R.id.rlFeedBack)).setOnClickListener(this);
    }

    /**
     * 更多
     *
     * @param view
     */
    public void doIvMore(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlItem2:

                break;
            case R.id.rlItem3:

                break;
            case R.id.rlItem4:

                break;
            case R.id.rlItem5:

                break;
            case R.id.rlAllQuestion:

                break;
            case R.id.rlQuickHelp:

                break;
            case R.id.rlFeedBack:

                break;
        }
    }

}
