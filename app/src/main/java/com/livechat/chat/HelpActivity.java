package com.livechat.chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * 帮助
 */
public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    /**
     * 拨打电话
     *
     * @param v
     */
    public void doTelPhone(View v) {
        final AlertDialog mDialog = new AlertDialog.Builder(this, R.style.MyDialog).create();
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
        // 中文提示
        TextView mChinaMsg = (TextView) view.findViewById(R.id.mChinaMsg);
        mChinaMsg.setText("是否拨打？");
        // 取消的按钮
        Button noBtn = (Button) view.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        // 确认的按钮
        Button yesBtn = (Button) view.findViewById(R.id.yesBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:13286082828"));
                if (ActivityCompat.checkSelfPermission(HelpActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });

        mDialog.setView(view, 0, 0, 0, 0);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        // 得到当前显示设备的宽度
        int mWidth = getWindowManager().getDefaultDisplay().getWidth();
        // 得到此AlertDialog界面的参数对象
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        // 设置AlertDialog的界面宽度
        params.width = mWidth - (mWidth / 6);
        // 设置AlertDialog高度为包裹内容
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置AlertDialog的重心
        params.gravity = Gravity.CENTER;
        // 设置AlertDialog的大小, 不能设置重心参数, 推荐用Attributes设置
        mDialog.getWindow().setLayout(mWidth - (mWidth / 4), ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setAttributes(params);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_tab_pay_close, R.anim.activity_tab_pay_close);
    }

}
