package com.android.leezp.learncarproject.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.leezp.learncarproject.activity.PublishOrderActivity;
import com.android.leezp.learncarproject.utils.Binder.PublishOrderBinder;

public class PublishOrderService extends Service {
    private PublishOrderBinder binder = new PublishOrderBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (binder.getReferenceActivity() != null) {
            PublishOrderActivity activity = binder.getReferenceActivity().get();
            if (activity != null) {
                activity.getPublishOrder();
            }
        }
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = System.currentTimeMillis() + 5 * 1000;
        Intent loopIntent = new Intent(this, PublishOrderService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, loopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        binder = null;
        super.onDestroy();
    }
}
