package com.android.leezp.learncarproject.presenter;

import com.android.leezp.learncarproject.db.LoginDB;
import com.android.leezp.learncarproject.db.manager.DataBaseManager;
import com.android.leezp.learncarproject.presenter.interfaces.Presenter;
import com.android.leezp.learncarproject.utils.event.NetEvent;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LoginPresenter implements Presenter{
    private CompositeSubscription subscription;
    private NetEvent event;
    private LoginDB loginData;

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

    public void requestStudentLogin(String phone, String password) {
        subscription.add(DataBaseManager.getManager().getStudentLogin(phone, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<LoginDB>() {
            @Override
            public void onCompleted() {
                event.onSuccess(loginData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("数据请求异常！");
            }

            @Override
            public void onNext(LoginDB loginDB) {
                loginData = loginDB;
            }
        }));
    }
}
