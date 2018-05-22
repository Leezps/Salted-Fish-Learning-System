package com.android.leezp.learncartrainproject;

import org.litepal.LitePalApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends LitePalApplication {
    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
}
