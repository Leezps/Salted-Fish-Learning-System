package com.android.leezp.learncarproject.presenter;

import com.android.leezp.learncarproject.db.RegisterDB;
import com.android.leezp.learncarproject.db.manager.DataBaseManager;
import com.android.leezp.learncarproject.presenter.interfaces.Presenter;
import com.android.leezp.learncarproject.utils.event.NetEvent;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class RegisterPresenter implements Presenter{
    private CompositeSubscription subscription;
    private NetEvent event;
    private RegisterDB registerData;

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

    public void requestVerification(String code, String phone) {
        subscription.add(DataBaseManager.getManager().getVerification(code, phone)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<RegisterDB>() {
            @Override
            public void onCompleted() {
                event.onSuccess(registerData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("数据请求异常！");
            }

            @Override
            public void onNext(RegisterDB registerDB) {
                registerData = registerDB;
            }
        }));
    }

    public void registerStudent(Map<String, String> params) {
        subscription.add(DataBaseManager.getManager().regisetrStudent(params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<RegisterDB>() {
            @Override
            public void onCompleted() {
                event.onSuccess(registerData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("数据请求异常！");
            }

            @Override
            public void onNext(RegisterDB registerDB) {
                registerData = registerDB;
            }
        }));
    }
}
