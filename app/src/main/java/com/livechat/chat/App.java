package com.livechat.chat;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.livechat.chat.db.LiveChatDefaultDBHelper;
import com.livechat.chat.service.LocationService;
import com.livechat.chat.utils.CommonUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import io.rong.imkit.RongIM;

/**
 * 程序的启动
 * Created by Administrator on 2016/4/14.
 */
public class App extends Application {

    private static App instance;
    private String sUserLoginId;// 用户唯一性
    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
        }

        /**
         * 初始化设置
         */
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        /**
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        /**
         * 百度推送
         */
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, CommonUtil.getMetaValue(this, "api_key"));

    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获得实例
     *
     * @return
     */
    public static App getInstance() {
        return instance;
    }

    /**
     * 设置用户的唯一性
     *
     * @param sUserLoginId
     */
    public void setsUserLoginId(String sUserLoginId) {
        if (!"".equals(sUserLoginId) && sUserLoginId != null) {
            this.sUserLoginId = sUserLoginId;
            // 连接数据库并且创建数据库以及数据表结构
            LiveChatDefaultDBHelper.getInstance(this).connectDB();
        }
    }

    /**
     * 获得用户的唯一性
     *
     * @return
     */
    public String getsUserLoginId() {
        if (sUserLoginId != null) {
            return sUserLoginId;
        }
        return "";
    }

}
