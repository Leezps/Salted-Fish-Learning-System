package com.android.leezp.learncartrainproject.presenter;

import com.android.leezp.learncartrainproject.MyApplication;
import com.android.leezp.learncartrainproject.activities.HomePagerActivity;
import com.android.leezp.learncartrainproject.entities.PublishOrderEntity;
import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.interfaces.Presenter;
import com.android.leezp.learncartrainproject.net.DataBaseManager;
import com.android.leezp.learncartrainproject.net.data.NetPublishOrderData;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class HomePagerPresenter implements Presenter {
    private CompositeSubscription subscription = null;
    private NetEvent event = null;
    private NetPublishOrderData orderData = null;

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

    public void getPublishOrderByDriverID(String driverID) {
        subscription.add(DataBaseManager.getManager().getPublishOrderByDriverID("1", "0", driverID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NetPublishOrderData>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(orderData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("获取教练的所有订单出现异常！");
                    }

                    @Override
                    public void onNext(NetPublishOrderData netPublishOrderData) {
                        orderData = netPublishOrderData;
                    }
                }));
    }

    public void publishOrder(Map<String, String> params) {
        subscription.add(DataBaseManager.getManager().publishOrder(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NetPublishOrderData>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(orderData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("订单发布出现异常！");
                    }

                    @Override
                    public void onNext(NetPublishOrderData netPublishOrderData) {
                        orderData = netPublishOrderData;
                    }
                }));
    }

    public void analyzeObject(Object object) {
        MyApplication.cachedThreadPool.execute(new HomePagerRunnable(this, object));
    }

    private static class HomePagerRunnable implements Runnable {
        private WeakReference<HomePagerPresenter> referencePresenter;
        private WeakReference<Object> referenceObject;

        public HomePagerRunnable(HomePagerPresenter presenter, Object object) {
            referencePresenter = new WeakReference<>(presenter);
            referenceObject = new WeakReference<>(object);
        }

        @Override
        public void run() {
            if (referenceObject.get() == null) {
                return;
            }
            if (referenceObject.get() instanceof NetPublishOrderData) {
                analyzeNetPublishOrderData();
            }
        }

        private void analyzeNetPublishOrderData() {
            List<PublishOrderEntity> entities = ((NetPublishOrderData) referenceObject.get()).getEntities();
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            List<String> dateData = new ArrayList<>();
            Map<String, List<String>> timeSlotData = new HashMap<>();
            if (entities != null && entities.size() > 0) {
                for (PublishOrderEntity entity : entities) {
                    try {
                        String date_str = dateFormat.format(timeStampFormat.parse(entity.getBeginTime()));
                        if (!dateData.contains(date_str)) {
                            dateData.add(date_str);
                        }
                        String begin_time = timeFormat.format(timeStampFormat.parse(entity.getBeginTime()));
                        String end_time = timeFormat.format(timeStampFormat.parse(entity.getEndTime()));
                        List<String> slot = timeSlotData.get(date_str);
                        if (slot == null) {
                            slot = new ArrayList<>();
                        }
                        slot.add(begin_time + "-" + end_time);
                        timeSlotData.put(date_str, slot);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            ((HomePagerActivity) referencePresenter.get().event).refreshPublishOrderContainer(dateData, timeSlotData);
        }
    }
}
