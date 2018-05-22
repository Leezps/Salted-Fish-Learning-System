package com.android.leezp.learncarproject.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.leezp.learncarproject.activity.HomePagerActivity;
import com.android.leezp.learncarproject.utils.Binder.HomePagerBinder;

public class HomePagerService extends Service {
    private HomePagerBinder homePagerBinder = new HomePagerBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return homePagerBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (homePagerBinder.getReferenceActivity() != null) {
            HomePagerActivity homePagerActivity = homePagerBinder.getReferenceActivity().get();
            if (homePagerActivity != null && homePagerActivity.getStudentEntity() != null) {
                homePagerActivity.userLoginSuccessRequest();
            }
        }
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = System.currentTimeMillis() + 10 * 1000;
        Intent loopIntent = new Intent(this, HomePagerService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, loopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        homePagerBinder = null;
        super.onDestroy();
    }
}
