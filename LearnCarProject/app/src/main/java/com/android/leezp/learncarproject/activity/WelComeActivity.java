package com.android.leezp.learncarproject.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.android.leezp.learncarlib.activity.BaseFragmentActivity;
import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.android.leezp.learncarproject.fragment.SplashFragment;

import org.litepal.LitePal;

import java.lang.ref.WeakReference;

/**
 * Created by Leezp on 2018/3/16 0016.
 * <p>
 * 每次打开应用的欢迎界面
 */

public class WelComeActivity extends BaseFragmentActivity {

    private static final String FIRST_OPEN = "isFirst";
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    private boolean isFirst;
    private SplashFragment splashFragment;
    private MyHandler handler;
    private TextView time;

    @Override
    protected void initVariables() {
        ActivityCollector.addActivity(this);
        preferences = getSharedPreferences(ActivityConstantEntity.welcomeActivity_sharePreferences_name, MODE_PRIVATE);
        edit = preferences.edit();
        //获取数据是否是第一次安装此应用
        isFirst = preferences.getBoolean(FIRST_OPEN, true);
        handler = new MyHandler(this);
        LitePal.getDatabase();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);
        time = ((TextView) findViewById(R.id.activity_welcome_time));
        //如果是第一次安装应用，则启动引导界面
        if (isFirst) {
            splashFragment = new SplashFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.activity_welcome_container, splashFragment);
            transaction.commit();
            isFirst = false;
            edit.putBoolean(FIRST_OPEN, isFirst);
            edit.apply();
        } else {
            startTimeView();
        }
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        edit.clear();
        ActivityCollector.removeActivity(this);
    }

    /**
     * 开始时间控件运行
     */
    private void startTimeView() {
        Message message = Message.obtain();
        time.setText("3");
        message.what = 3;
        handler.sendMessageDelayed(message, 1000);
    }

    /**
     * 关闭引导界面
     */
    public void closeSplashFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(splashFragment);
        transaction.commit();
        startTimeView();
    }

    /**
     * 设置修改时间控件的方法
     * @param num
     */
    public void  changeTimeView(int num) {
        //做限制，只有0-10秒以内的才可以设置到控件上
        if (num >= 0 && num <= 10) {
            time.setText(String.valueOf(num));
        } else {
            time.setText("0");
        }
    }

    /**
     * 静态Handler，防止内存泄漏
     */
    private static class MyHandler extends Handler {
        WeakReference<WelComeActivity> activities;

        public MyHandler(WelComeActivity activity) {
            activities = new WeakReference<WelComeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == 1) {
                HomePagerActivity.startHomePagerActivity(activities.get());
                ActivityCollector.removeActivity(activities.get());
                activities.get().finish();
            } else {
                what = what-1;
                Message message = Message.obtain();
                message.what = what;
                activities.get().changeTimeView(what);
                sendMessageDelayed(message, 1000);
            }
        }
    }
}
