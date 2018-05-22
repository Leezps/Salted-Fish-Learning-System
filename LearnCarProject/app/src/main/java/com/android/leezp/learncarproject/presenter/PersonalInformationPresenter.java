package com.android.leezp.learncarproject.presenter;

import com.android.leezp.learncarproject.db.PersonalInformationDB;
import com.android.leezp.learncarproject.db.manager.DataBaseManager;
import com.android.leezp.learncarproject.presenter.interfaces.Presenter;
import com.android.leezp.learncarproject.utils.event.NetEvent;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class PersonalInformationPresenter implements Presenter {
    private CompositeSubscription subscription;
    private NetEvent event;
    private PersonalInformationDB informationData;

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

    public void uploadImage(Map<String, String> params, RequestBody image) {
        subscription.add(DataBaseManager.getManager().uploadImage(params, image)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<PersonalInformationDB>() {
            @Override
            public void onCompleted() {
                event.onSuccess(informationData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("数据请求异常！");
            }

            @Override
            public void onNext(PersonalInformationDB personalInformationDB) {
                informationData = personalInformationDB;
            }
        }));
    }

    public void uploadPersonalInformation(RequestBody information) {
        subscription.add(DataBaseManager.getManager().uploadPersonalInformation(information)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<PersonalInformationDB>() {
            @Override
            public void onCompleted() {
                event.onSuccess(informationData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("数据请求异常！");
            }

            @Override
            public void onNext(PersonalInformationDB personalInformationDB) {
                informationData = personalInformationDB;
            }
        }));
    }
}
