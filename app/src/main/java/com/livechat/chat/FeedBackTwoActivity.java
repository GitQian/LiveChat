package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.livechat.chat.utils.Constant;
import com.livechat.chat.utils.DateTimePickDialogUtil;
import com.livechat.chat.widget.DropDownPopu;

import java.util.Calendar;

/**
 * 填写问题的意见反馈
 */
public class FeedBackTwoActivity extends BaseActivity {

    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    private TextView tvContent;
    private EditText etTime;
    private EditText etPhoneNumber;

    private String initStartDateTime = "";// 初始化开始时间
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_two);

        initUI();

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        initStartDateTime = mYear + "年" + mMonth + "月" + mDay + "日 " + mHour + ":" + mMinute;
        etTime.setText(initStartDateTime);
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(FeedBackTwoActivity.this, initStartDateTime);
                dateTimePicKDialog.dateTimePicKDialog(etTime);
            }
        });
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        checkBox1 = (CheckBox) this.findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) this.findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) this.findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) this.findViewById(R.id.checkBox4);
        tvContent = (TextView) this.findViewById(R.id.tvContent);
        etTime = (EditText) this.findViewById(R.id.etTime);
        etPhoneNumber = (EditText) this.findViewById(R.id.etPhoneNumber);
    }

    /**
     * 补充内容
     *
     * @param view
     */
    public void doQuestContent(View view) {
        Intent intent = new Intent(this, FeedBackThreeActivity.class);
        startActivityForResult(intent, Constant.CONTENT_CODE);
    }

    /**
     * 提交
     *
     * @param view
     */
    public void doBtnSubmit(View view) {

    }

    /**
     * 更多
     *
     * @param view
     */
    public void doIvMore(View view) {
        new DropDownPopu().showPopupWindow(view, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.CONTENT_CODE) {
                String sFeedBackContent = data.getStringExtra("sFeedBackContent");
                tvContent.setText(sFeedBackContent);
            }
        }
    }

}
