package com.android.leezp.learncarproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.activity.WelComeActivity;
import com.android.leezp.learncarproject.adapter.SplashImagesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leezp on 2018/3/19 0019.
 * <p>
 * 最开始安装应用的引导界面
 */

public class SplashFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private List<Integer> images;
    private int nowIndex = 0;
    private View view;
    private RadioGroup indexGroup;
    private TextView gotoBtn;
    private ViewPager viewImages;
    private SplashImagesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化控件
        view = inflater.inflate(R.layout.fragment_splash, container, false);
        viewImages = (ViewPager) view.findViewById(R.id.fragment_splash_images);
        indexGroup = (RadioGroup) view.findViewById(R.id.fragment_splash_index);
        gotoBtn = (TextView) view.findViewById(R.id.fragment_splash_goto_welcome_btn);

        //初始化资源图片
        setImages();
        adapter = new SplashImagesAdapter(getContext(), images);

        //初始化界面的数据，动态给图片加载引导小圆圈
        for (int i = 0; i < images.size(); ++i) {
            RadioButton radioButton = new RadioButton(container.getContext());
            radioButton.setButtonDrawable(null);
            radioButton.setClickable(false);
            radioButton.setBackgroundResource(R.drawable.splash_index_selector);
            radioButton.setWidth(15);
            radioButton.setHeight(15);
            indexGroup.addView(radioButton);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) radioButton.getLayoutParams();
            layoutParams.setMargins(20, 20, 20, 20);
        }
        ((RadioButton) indexGroup.getChildAt(nowIndex)).setChecked(true);


        //初始化动态响应的点击事件
        gotoBtn.setOnClickListener(this);
        viewImages.setAdapter(adapter);
        viewImages.addOnPageChangeListener(this);

        return view;
    }

    /**
     * 设置进入应用的按钮的点击事件处理方式
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_splash_goto_welcome_btn:
                ((WelComeActivity) getActivity()).closeSplashFragment();
                break;
        }
    }

    /**
     * 给ImagesAdapter设置图片资源
     */
    private void setImages() {
        images = new ArrayList<>();
        images.add(R.drawable.first_open_one);
        images.add(R.drawable.first_open_two);
        images.add(R.drawable.first_open_three);
    }

    /**
     * 下面时ViewPager滑动的响应事件
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton child = (RadioButton) indexGroup.getChildAt(position);
        child.setChecked(true);
        if (position == images.size() - 1 && gotoBtn.getVisibility() == View.INVISIBLE) {
            gotoBtn.setVisibility(View.VISIBLE);
        } else if (gotoBtn.getVisibility() == View.VISIBLE) {
            gotoBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
