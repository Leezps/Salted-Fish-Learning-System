package com.android.leezp.learncartrainproject.presenter;

import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.interfaces.Presenter;
import com.android.leezp.learncartrainproject.net.DataBaseManager;
import com.android.leezp.learncartrainproject.net.data.NetMessageCenterData;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MessageCenterPresenter implements Presenter{
    private CompositeSubscription subscription;
    private NetEvent event;
    private NetMessageCenterData messageCenterData;

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

    public void getMessageCenter(String driverID, String phone, String password) {
        subscription.add(DataBaseManager.getManager().getMessageCenter(driverID, phone, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<NetMessageCenterData>() {
            @Override
            public void onCompleted() {
                event.onSuccess(messageCenterData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("获取消息中心数据异常！");
            }

            @Override
            public void onNext(NetMessageCenterData netMessageCenterData) {
                messageCenterData = netMessageCenterData;
            }
        }));
    }

    public void updateMessageCenter(String driverID, String phone, String password, final String messageID) {
        subscription.add(DataBaseManager.getManager().updateMessageCenter(driverID, phone, password, messageID)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<NetMessageCenterData>() {
            @Override
            public void onCompleted() {
                event.onSuccess(messageCenterData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("更新消息中心数据异常！");
            }

            @Override
            public void onNext(NetMessageCenterData netMessageCenterData) {
                messageCenterData = netMessageCenterData;
            }
        }));
    }

    public void removeMessageCenter(String driverID, String phone, String password, String messageID) {
        subscription.add(DataBaseManager.getManager().removeMessageCenter(driverID, phone, password, messageID)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<NetMessageCenterData>() {
            @Override
            public void onCompleted() {
                event.onSuccess(messageCenterData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("删除消息中心数据异常！");
            }

            @Override
            public void onNext(NetMessageCenterData netMessageCenterData) {
                messageCenterData = netMessageCenterData;
            }
        }));
    }
}
