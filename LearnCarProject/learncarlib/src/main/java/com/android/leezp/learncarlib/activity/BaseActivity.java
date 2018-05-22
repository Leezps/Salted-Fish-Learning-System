package com.android.leezp.learncarlib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Leezp on 2018/3/16 0016.
 */

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    //初始化数据
    protected abstract void initVariables();

    //初始化控件
    protected abstract void initViews(Bundle savedInstanceState);

    //网络加载数据
    protected abstract void loadData();

}
