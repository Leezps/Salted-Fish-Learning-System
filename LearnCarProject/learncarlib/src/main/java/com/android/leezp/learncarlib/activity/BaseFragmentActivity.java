package com.android.leezp.learncarlib.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Leezp on 2018/3/19 0019.
 */

public abstract class BaseFragmentActivity extends FragmentActivity {

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
