package com.android.leezp.learncarproject.utils.Binder;

import android.os.Binder;

import com.android.leezp.learncarproject.activity.HomePagerActivity;

import java.lang.ref.WeakReference;

public class HomePagerBinder extends Binder {
    private WeakReference<HomePagerActivity> referenceActivity;

    public WeakReference<HomePagerActivity> getReferenceActivity() {
        return referenceActivity;
    }

    public void setReferenceActivity(HomePagerActivity activity) {
        referenceActivity = new WeakReference<>(activity);
    }
}
