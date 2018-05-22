package com.android.leezp.learncarproject.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Leezp on 2018/3/31.
 */

public class SplashImagesAdapter extends PagerAdapter {
    private Context context;
    private List<Integer> images;

    public SplashImagesAdapter(Context context, List<Integer> data) {
        super();
        this.context = context;
        images = data;
    }

    /**
     * 返回页面数量
     */
    @Override
    public int getCount() {
        return images == null ? 0 : images.size();
    }

    /**
     * 判断该界面是否由对象生成的界面
     *
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object != null) {
            int count = container.getChildCount();
            for (int i = 0; i < count; ++i) {
                View child = container.getChildAt(i);
                if (child == object) {
                    container.removeView(child);
                    break;
                }
            }
        }
    }

    /**
     * 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (images != null && position < images.size()) {
            Integer resId = images.get(position);
            if (resId != null) {
                ImageView image = new ImageView(context);
                image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                image.setImageResource(resId);
                image.setTag(resId);
                container.addView(image);
                return  image;
            }
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object != null && images != null) {
            Integer resId = (Integer)((ImageView)object).getTag();
            if (resId != null) {
                for (int i = 0; i < images.size(); i++) {
                    if (resId.equals(images.get(i))) {
                        return i;
                    }
                }
            }
        }
        return POSITION_NONE;
    }
}
