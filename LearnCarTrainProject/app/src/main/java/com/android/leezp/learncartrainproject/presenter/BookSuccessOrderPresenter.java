package com.android.leezp.learncartrainproject.presenter;

import com.android.leezp.learncartrainproject.entities.BookSuccessOrderEntity;
import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.interfaces.Presenter;
import com.android.leezp.learncartrainproject.net.DataBaseManager;
import com.android.leezp.learncartrainproject.net.data.NetBookSuccessOrderData;
import com.android.leezp.learncartrainproject.net.data.NetRelationBookSuccessOrderData;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BookSuccessOrderPresenter implements Presenter {
    private CompositeSubscription subscription;
    private NetEvent event;
    private NetBookSuccessOrderData bookSuccessOrderData;
    private NetRelationBookSuccessOrderData relationBookSuccessOrderData;

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

    public void getBookSuccessOrder(String driverID, String phone, String password) {
        subscription.add(DataBaseManager.getManager().getBookSuccessOrder(driverID, phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NetBookSuccessOrderData>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(bookSuccessOrderData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("查询预约完成订单数据请求异常！");
                    }

                    @Override
                    public void onNext(NetBookSuccessOrderData netBookSuccessOrderData) {
                        bookSuccessOrderData = netBookSuccessOrderData;
                    }
                }));
    }

    public void getRelationBookSuccessOrder(String orderID, String driverID, String studentID) {
        subscription.add(DataBaseManager.getManager().getRelationBookSuccessOrder(orderID, driverID, studentID)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<NetRelationBookSuccessOrderData>() {
            @Override
            public void onCompleted() {
                event.onSuccess(relationBookSuccessOrderData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("获取预约完成订单的学员信息数据请求异常！");
            }

            @Override
            public void onNext(NetRelationBookSuccessOrderData netRelationBookSuccessOrderData) {
                relationBookSuccessOrderData = netRelationBookSuccessOrderData;
            }
        }));
    }


    public void removeBookSuccessOrder(String driverID, String phone, String password, String orderID, String removeMessage) {
        subscription.add(DataBaseManager.getManager().removeBookSuccessOrder(driverID, phone, password, orderID, removeMessage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NetBookSuccessOrderData>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(bookSuccessOrderData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("删除预约完成订单数据请求异常！");
                    }

                    @Override
                    public void onNext(NetBookSuccessOrderData netBookSuccessOrderData) {
                        bookSuccessOrderData = netBookSuccessOrderData;
                    }
                }));
    }
}
