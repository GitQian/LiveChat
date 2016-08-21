package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 版本
     *
     * @param view
     */
    public void doVersion(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    /**
     * 设置
     *
     * @param view
     */
    public void doSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    /**
     * 个人信息
     *
     * @param view
     */
    public void doPersonInfo(View view) {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        startActivity(intent);
    }

    /**
     * 上传图片
     *
     * @param view
     */
    public void doUploadPic(View view) {
        /**
         * 上传图片到服务器
         *
         * @param mContext 上下文
         * @param path     要上传的文件路径
         * @param url      服务端接收URL
         */

        String path = "/storage/sdcard0/DCIM/Camera/IMG_20160506_142431.jpg";
        CommonUtil.uploadPicToServer(this, path, Constant.sUploadImageUrl);
    }

    /**
     * 下载图片
     *
     * @param view
     */
    public void doDownloadPic(View view) {
        String path = "/storage/sdcard0/DCIM/Camera/IMG_20160506_142431_000.jpg";
        CommonUtil.downLoadPicFromServer(this, Constant.sUploadImageUrl + path);
    }

}
