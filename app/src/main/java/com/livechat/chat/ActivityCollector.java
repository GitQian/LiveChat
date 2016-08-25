package com.livechat.chat;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/8/25.
 */
public class ActivityCollector {
    public static List<Activity> activitys = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activitys.add(activity);
    }

    public static void removeActivity(Activity activity){
        activitys.remove(activity);
    }

    public static void finishAllActivity() {
        for (Activity activity : activitys) {
            activity.finish();
        }
    }
}
