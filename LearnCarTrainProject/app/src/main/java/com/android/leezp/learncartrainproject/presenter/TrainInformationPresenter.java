package com.android.leezp.learncartrainproject.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.leezp.learncartrainproject.MyApplication;
import com.android.leezp.learncartrainproject.activities.TrainInformationActivity;
import com.android.leezp.learncartrainproject.entities.DriverEntity;
import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.interfaces.Presenter;
import com.android.leezp.learncartrainproject.net.DataBaseManager;
import com.android.leezp.learncartrainproject.net.data.NetDriverData;
import com.android.leezp.learncartrainproject.utils.ProjectConstant;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class TrainInformationPresenter implements Presenter {
    private CompositeSubscription subscription;
    private NetEvent event;
    private NetDriverData driverData;

    @Override
    public void onCreate() {
        subscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        if (subscription.hasSubscriptions()) {
            subscription.unsubscribe();
        }
    }

    public void attachEvent(NetEvent event) {
        this.event = event;
    }

    public void uploadOrRenewImage(Map<String, String> params, RequestBody image) {
        subscription.add(DataBaseManager.getManager().uploadOrRenewImage(params, image)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<NetDriverData>() {
            @Override
            public void onCompleted() {
                event.onSuccess(driverData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("上传图片信息数据请求异常！");
            }

            @Override
            public void onNext(NetDriverData netDriverData) {
                driverData = netDriverData;
            }
        }));
    }

    public void uploadOrRenewTrainerInformation(RequestBody information) {
        subscription.add(DataBaseManager.getManager().uploadOrRenewTrainerInformation(information)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<NetDriverData>() {
            @Override
            public void onCompleted() {
                event.onSuccess(driverData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("上传个人信息数据请求异常！");
            }

            @Override
            public void onNext(NetDriverData netDriverData) {
                driverData = netDriverData;
            }
        }));
    }

    public void storageDriver(DriverEntity driverEntity) {
        MyApplication.cachedThreadPool.execute(new TrainInformationRunnable(this, driverEntity));
    }

    private static class TrainInformationRunnable implements Runnable {
        private WeakReference<TrainInformationPresenter> referencePresenter;
        private WeakReference<DriverEntity> referenceEntity;

        public TrainInformationRunnable(TrainInformationPresenter presenter, DriverEntity entity) {
            referencePresenter = new WeakReference<>(presenter);
            referenceEntity = new WeakReference<>(entity);
        }

        @Override
        public void run() {
            if (referenceEntity != null) {
                SharedPreferences preferences = ((TrainInformationActivity) referencePresenter.get().event).getSharedPreferences(ProjectConstant.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(ProjectConstant.PERSONAL_INFORMATION, new Gson().toJson(referenceEntity.get()));
                editor.apply();
            }
        }
    }
}
