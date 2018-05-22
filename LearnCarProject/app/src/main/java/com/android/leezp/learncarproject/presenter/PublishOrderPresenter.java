package com.android.leezp.learncarproject.presenter;

import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.activity.PublishOrderActivity;
import com.android.leezp.learncarproject.db.CompleteOrderDB;
import com.android.leezp.learncarproject.db.PublishOrderDB;
import com.android.leezp.learncarproject.db.manager.DataBaseManager;
import com.android.leezp.learncarproject.entity.CompleteOrderEntity;
import com.android.leezp.learncarproject.entity.PublishOrderEntity;
import com.android.leezp.learncarproject.presenter.interfaces.Presenter;
import com.android.leezp.learncarproject.utils.event.NetEvent;

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

public class PublishOrderPresenter implements Presenter {
    private CompositeSubscription subscription = null;
    private WeakReference<PublishOrderActivity> referenceActivity;
    private NetEvent event = null;
    private PublishOrderDB orderData = null;
    private CompleteOrderDB completeOrderData = null;

    public PublishOrderPresenter(PublishOrderActivity activity) {
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

    public void attachEvent(NetEvent event) {
        this.event = event;
    }

    @Override
    public void onDestroy() {
        if (subscription.hasSubscriptions()) {
            subscription.unsubscribe();
        }
    }

    public void getPublishOrderByDriverID(String role, String code, String driverID) {
        subscription.add(DataBaseManager.getManager().getPublishOrderByDriverID(role, code, driverID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PublishOrderDB>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(orderData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("发布订单请求异常！");
                    }

                    @Override
                    public void onNext(PublishOrderDB publishOrderDB) {
                        orderData = publishOrderDB;
                    }
                }));
    }

    public void getCompOrderByPublishOrder(String requestCode, String publishOrderId) {
        subscription.add(DataBaseManager.getManager().getCompOrderByPublishOrder(requestCode, publishOrderId)
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
                        event.onError("发布订单中的完成订单获取异常！");
                    }

                    @Override
                    public void onNext(CompleteOrderDB completeOrderDB) {
                        completeOrderData = completeOrderDB;
                    }
                }));
    }

    public void studentBookOrder(Map<String, String> params) {
        subscription.add(DataBaseManager.getManager().studentBookOrder(params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<PublishOrderDB>() {
            @Override
            public void onCompleted() {
                event.onSuccess(orderData);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                event.onError("预订订单请求异常！");
            }

            @Override
            public void onNext(PublishOrderDB publishOrderDB) {
                orderData = publishOrderDB;
            }
        }));
    }

    /**
     * 解析数据
     *
     * @param object 数据对象
     */
    public void analyzeObject(Object object) {
        ActivityCollector.cachedThreadPool.execute(new PublishOrderRunnable(this, object));
    }

    private static class PublishOrderRunnable implements Runnable {
        private WeakReference<PublishOrderPresenter> referencePresenter;
        private WeakReference<Object> referenceObject;

        public PublishOrderRunnable(PublishOrderPresenter presenter, Object object) {
            referencePresenter = new WeakReference<>(presenter);
            referenceObject = new WeakReference<>(object);
        }

        @Override
        public void run() {
            Object object = referenceObject.get();
            if (object instanceof PublishOrderDB) {
                analyzePublishOrderDB();
            } else if (object instanceof CompleteOrderDB) {
                analyzeCompleteOrderDB();
            }
        }

        private void analyzeCompleteOrderDB() {
            List<CompleteOrderEntity> entities = ((CompleteOrderDB) referenceObject.get()).getComplete_orders();
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            List<String> completeOrderTimeSlotData = new ArrayList<>();
            if (entities != null && entities.size() > 0) {
                for (CompleteOrderEntity entity : entities) {
                    try {
                        String begin_time = timeFormat.format(timeStampFormat.parse(entity.getBeginTime()));
                        String end_time = timeFormat.format(timeStampFormat.parse(entity.getEndTime()));
                        completeOrderTimeSlotData.add(begin_time + "-" + end_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            referencePresenter.get().referenceActivity.get().refreshCompleteOrderRecycler(completeOrderTimeSlotData);
        }

        private void analyzePublishOrderDB() {
            List<PublishOrderEntity> entities = ((PublishOrderDB) referenceObject.get()).getEntities();
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            List<String> dateData = new ArrayList<>();
            Map<String, List<String>> timeSlotData = new HashMap<>();
            Map<String, List<Integer>> publishOrderIdData = new HashMap<>();
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
                        List<Integer> orderId = publishOrderIdData.get(date_str);
                        if (orderId == null) {
                            orderId = new ArrayList<>();
                        }
                        orderId.add(entity.getNetId());
                        publishOrderIdData.put(date_str, orderId);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            referencePresenter.get().referenceActivity.get().refreshPublishOrderContainer(dateData, timeSlotData, publishOrderIdData);
        }
    }
}
