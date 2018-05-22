package com.android.leezp.learncarproject.utils.ServiceConnection;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.android.leezp.learncarproject.activity.PublishOrderActivity;
import com.android.leezp.learncarproject.utils.Binder.PublishOrderBinder;

public class PublishOrderServiceConnection implements ServiceConnection{
    private PublishOrderActivity activity;

    public PublishOrderServiceConnection(PublishOrderActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ((PublishOrderBinder) iBinder).setReferenceActivity(activity);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) { }
}
