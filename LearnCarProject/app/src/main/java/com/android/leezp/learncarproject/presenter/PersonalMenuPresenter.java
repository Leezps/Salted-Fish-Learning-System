package com.android.leezp.learncarproject.presenter;

import android.content.SharedPreferences;

import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.activity.PersonalMenuActivity;
import com.android.leezp.learncarproject.db.CompleteOrderDB;
import com.android.leezp.learncarproject.db.MessageCenterDB;
import com.android.leezp.learncarproject.db.PaymentWayDB;
import com.android.leezp.learncarproject.db.RelationCompleteOrderDB;
import com.android.leezp.learncarproject.db.manager.DataBaseManager;
import com.android.leezp.learncarproject.entity.PaymentWayEntity;
import com.android.leezp.learncarproject.entity.StudentEntity;
import com.android.leezp.learncarproject.presenter.interfaces.Presenter;
import com.android.leezp.learncarproject.utils.event.NetEvent;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class PersonalMenuPresenter implements Presenter {
    private CompositeSubscription subscription = null;
    private WeakReference<PersonalMenuActivity> referenceActivity = null;
    private NetEvent event = null;
    private MessageCenterDB messageData = null;
    private PaymentWayDB paymentData = null;
    private CompleteOrderDB completeOrderData = null;
    private RelationCompleteOrderDB relationOrderData = null;
    private SharedPreferences preferences = null;

    public PersonalMenuPresenter(PersonalMenuActivity activity) {
        this.referenceActivity = new WeakReference<>(activity);
    }

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

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void removeOrUpdateMessage(Map<String, String> params) {
        subscription.add(DataBaseManager.getManager().removeOrUpdateMessage(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MessageCenterDB>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(messageData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("数据请求异常！");
                    }

                    @Override
                    public void onNext(MessageCenterDB messageCenterDB) {
                        messageData = messageCenterDB;
                    }
                }));
    }

    public void removeOrAddPaymentWay(Map<String, String> params) {
        subscription.add(DataBaseManager.getManager().operationPaymentWay(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PaymentWayDB>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(paymentData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("数据请求异常！");
                    }

                    @Override
                    public void onNext(PaymentWayDB paymentWayDB) {
                        paymentData = paymentWayDB;
                    }
                }));
    }

    public void getRelationCompleteOrder(String role, String orderID, String driverID, String studentID) {
        subscription.add(DataBaseManager.getManager().getRelationCompleteOrder(role, orderID, driverID, studentID)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<RelationCompleteOrderDB>() {
            @Override
            public void onCompleted() {
                event.onSuccess(relationOrderData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("数据请求异常！");
            }

            @Override
            public void onNext(RelationCompleteOrderDB relationCompleteOrderDB) {
                relationOrderData = relationCompleteOrderDB;
            }
        }));
    }

    public void requestRemoveCompleteOrder(Map<String, String> params) {
        subscription.add(DataBaseManager.getManager().requestRemoveCompleteOrder(params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<CompleteOrderDB>() {
            @Override
            public void onCompleted() {
                event.onSuccess(completeOrderData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("数据请求异常！");
            }

            @Override
            public void onNext(CompleteOrderDB completeOrderDB) {
                completeOrderData = completeOrderDB;
            }
        }));
    }

    public void getUserInformation() {
        ActivityCollector.cachedThreadPool.execute(new PersonalMenuRunable(this));
    }

    public void storageDataBase(Object object) {
        ActivityCollector.cachedThreadPool.execute(new PersonalMenuRunable(this, object));
    }

    private static class PersonalMenuRunable implements Runnable {
        private WeakReference<PersonalMenuPresenter> reference = null;
        private WeakReference<Object> referenceObject = null;

        public PersonalMenuRunable(PersonalMenuPresenter presenter) {
            this.reference = new WeakReference<>(presenter);
        }

        public PersonalMenuRunable(PersonalMenuPresenter presenter, Object object) {
            this.reference = new WeakReference<>(presenter);
            this.referenceObject = new WeakReference<>(object);
        }

        @Override
        public void run() {
            if (referenceObject == null) {
                String information = reference.get().preferences.getString("personalInformation", null);
                if (information != null) {
                    StudentEntity studentEntity = new Gson().fromJson(information, StudentEntity.class);
                    reference.get().event.onSuccess(studentEntity);
                }
            } else if (referenceObject.get() instanceof PaymentWayDB) {
                List<PaymentWayEntity> entities = ((PaymentWayDB) referenceObject.get()).getPayment_way();
                DataSupport.deleteAll(PaymentWayEntity.class);
                if (entities != null && entities.size() > 0) {
                    for (PaymentWayEntity entity : entities) {
                        entity.save();
                    }
                }
                reference.get().referenceActivity.get().refreshPaymentWayFragment();
            }
        }
    }
}
