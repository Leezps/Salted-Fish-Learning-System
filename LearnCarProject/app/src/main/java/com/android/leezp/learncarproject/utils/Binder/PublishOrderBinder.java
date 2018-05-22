package com.android.leezp.learncarproject.utils.Binder;

import android.os.Binder;

import com.android.leezp.learncarproject.activity.PublishOrderActivity;

import java.lang.ref.WeakReference;

public class PublishOrderBinder extends Binder {
    private WeakReference<PublishOrderActivity> referenceActivity;

    public WeakReference<PublishOrderActivity> getReferenceActivity() {
        return referenceActivity;
    }

    public void setReferenceActivity(PublishOrderActivity activity) {
        this.referenceActivity = new WeakReference<>(activity);
    }
}
