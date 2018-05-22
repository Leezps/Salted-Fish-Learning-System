package com.android.leezp.learncarproject;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Leezp on 2018/3/16 0016.
 */

public class ActivityCollector {
    //用于存储当前存在的Activity
    public static List<Activity> activities = new ArrayList<>();
    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
