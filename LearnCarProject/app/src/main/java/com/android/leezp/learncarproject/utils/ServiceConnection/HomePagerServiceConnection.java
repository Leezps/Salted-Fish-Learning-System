package com.android.leezp.learncarproject.utils.ServiceConnection;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.android.leezp.learncarproject.activity.HomePagerActivity;
import com.android.leezp.learncarproject.utils.Binder.HomePagerBinder;

public class HomePagerServiceConnection implements ServiceConnection {
    private HomePagerActivity activity;

    public HomePagerServiceConnection(HomePagerActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ((HomePagerBinder) iBinder).setReferenceActivity(activity);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) { }
}
