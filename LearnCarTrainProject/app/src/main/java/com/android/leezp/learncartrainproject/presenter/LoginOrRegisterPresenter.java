package com.android.leezp.learncartrainproject.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.leezp.learncartrainproject.MyApplication;
import com.android.leezp.learncartrainproject.activities.LoginOrRegisterActivity;
import com.android.leezp.learncartrainproject.entities.DriverEntity;
import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.interfaces.Presenter;
import com.android.leezp.learncartrainproject.net.DataBaseManager;
import com.android.leezp.learncartrainproject.net.data.NetDriverData;
import com.android.leezp.learncartrainproject.utils.ProjectConstant;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LoginOrRegisterPresenter implements Presenter{
    private CompositeSubscription subscription = null;
    private NetEvent event = null;
    private NetDriverData driverData = null;

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

    public void loginDriver(String phone, String password) {
        subscription.add(DataBaseManager.getManager().loginDriver(phone, password)
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
                event.onError("教练登陆出现异常！");
            }

            @Override
            public void onNext(NetDriverData netDriverData) {
                driverData = netDriverData;
            }
        }));
    }

    public void registerGetVerification(String phone) {
        subscription.add(DataBaseManager.getManager().registerGetVerification("0", phone)
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
                event.onError("教练注册获取验证码出现异常！");
            }

            @Override
            public void onNext(NetDriverData netDriverData) {
                driverData = netDriverData;
            }
        }));
    }

    public void registerDriver(Map<String, String> params) {
        subscription.add(DataBaseManager.getManager().registerDriver(params)
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
                event.onError("教练注册出现异常！");
            }

            @Override
            public void onNext(NetDriverData netDriverData) {
                driverData = netDriverData;
            }
        }));
    }

    public void storageDriver(DriverEntity driverEntity) {
        MyApplication.cachedThreadPool.execute(new LoginOrRegisterRunnable(this, driverEntity));
    }

    private static class LoginOrRegisterRunnable implements Runnable {
        private WeakReference<LoginOrRegisterPresenter> referencePresenter;
        private WeakReference<DriverEntity> referenceEntity;

        public LoginOrRegisterRunnable(LoginOrRegisterPresenter presenter, DriverEntity entity) {
            referencePresenter = new WeakReference<>(presenter);
            referenceEntity = new WeakReference<>(entity);
        }

        @Override
        public void run() {
            if (referenceEntity.get() != null) {
                SharedPreferences preferences = ((LoginOrRegisterActivity) referencePresenter.get().event).getSharedPreferences(ProjectConstant.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(ProjectConstant.PERSONAL_INFORMATION, new Gson().toJson(referenceEntity.get()));
                editor.apply();
            }
        }
    }
}
