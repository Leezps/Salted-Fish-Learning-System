package com.android.leezp.learncarproject.presenter;

import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.activity.HomePagerActivity;
import com.android.leezp.learncarproject.db.BaiduMapDB;
import com.android.leezp.learncarproject.db.CompleteOrderDB;
import com.android.leezp.learncarproject.db.DriverProcessDB;
import com.android.leezp.learncarproject.db.DriverShowDB;
import com.android.leezp.learncarproject.db.ExamCheatsDB;
import com.android.leezp.learncarproject.db.HelpDB;
import com.android.leezp.learncarproject.db.LoginDB;
import com.android.leezp.learncarproject.db.MessageCenterDB;
import com.android.leezp.learncarproject.db.PaymentWayDB;
import com.android.leezp.learncarproject.db.manager.DataBaseManager;
import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.android.leezp.learncarproject.entity.CompleteOrderEntity;
import com.android.leezp.learncarproject.entity.DriverEntity;
import com.android.leezp.learncarproject.entity.DriverProcessEntity;
import com.android.leezp.learncarproject.entity.ExamCheatsEntity;
import com.android.leezp.learncarproject.entity.HelpEntity;
import com.android.leezp.learncarproject.entity.MessageCenterEntity;
import com.android.leezp.learncarproject.entity.PaymentWayEntity;
import com.android.leezp.learncarproject.presenter.interfaces.Presenter;
import com.android.leezp.learncarproject.utils.event.NetEvent;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class HomePagerPresenter implements Presenter {

    private CompositeSubscription subscription;
    private NetEvent event;
    private WeakReference<HomePagerActivity> referenceActivity;
    private DriverProcessDB processData;
    private LoginDB loginData;
    private ExamCheatsDB examData;
    private HelpDB helpData;
    private MessageCenterDB messageData;
    private PaymentWayDB paymentData;
    private DriverShowDB driverShowData;
    private CompleteOrderDB completeData;
    private BaiduMapDB baiduMapData;
    /**
     * 用来计算学车流程总共步骤，当考试秘籍根据步骤存储完时，发起更新考试秘籍内容区
     */
    private int driverProcessSize = 0;

    public HomePagerPresenter(HomePagerActivity activity) {
        referenceActivity = new WeakReference<>(activity);
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

    public void requestDriverProcess() {
        subscription.add(DataBaseManager.getManager().requestDriverProcess()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DriverProcessDB>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(processData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("学车流程数据请求异常！");
                    }

                    @Override
                    public void onNext(DriverProcessDB processDB) {
                        processData = processDB;
                    }
                }));
    }

    public void requestUserLogined(String phone, String password) {
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
                        event.onError("用户登录数据请求异常！");
                    }

                    @Override
                    public void onNext(LoginDB loginDB) {
                        loginData = loginDB;
                    }
                }));
    }

    public void requestExamCheats(String processOrder) {
        subscription.add(DataBaseManager.getManager().requestExamCheats(processOrder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ExamCheatsDB>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(examData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("考试秘籍数据请求异常！");
                    }

                    @Override
                    public void onNext(ExamCheatsDB examCheatsDB) {
                        examData = examCheatsDB;
                    }
                }));
    }

    public void getHelp() {
        subscription.add(DataBaseManager.getManager().getHelp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HelpDB>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(helpData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("应用帮助数据请求异常！");
                    }

                    @Override
                    public void onNext(HelpDB helpDB) {
                        helpData = helpDB;
                    }
                }));
    }

    public void getMessageCenter(Map<String, String> params) {
        subscription.add(DataBaseManager.getManager().getMessageCenter(params)
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
                        event.onError("消息中心数据请求异常！");
                    }

                    @Override
                    public void onNext(MessageCenterDB messageCenterDB) {
                        messageData = messageCenterDB;
                    }
                }));
    }

    public void getCompleteOrders(String requestCode, String role, String id, String phone, String password) {
        subscription.add(DataBaseManager.getManager().getCompleteOrders(requestCode, role, id, phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompleteOrderDB>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(completeData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("预约成功订单数据请求异常！");
                    }

                    @Override
                    public void onNext(CompleteOrderDB completeOrderDB) {
                        completeData = completeOrderDB;
                    }
                }));
    }

    public void getPaymentWays(Map<String, String> params) {
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
                        event.onError("支付方式数据请求异常！");
                    }

                    @Override
                    public void onNext(PaymentWayDB paymentWayDB) {
                        paymentData = paymentWayDB;
                    }
                }));
    }

    public void findDriverOrderByPlace(String place) {
        subscription.add(DataBaseManager.getManager().findDriverOrderByPlace(place)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DriverShowDB>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(driverShowData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("教练信息数据请求异常！");
                    }

                    @Override
                    public void onNext(DriverShowDB driverShowDB) {
                        driverShowData = driverShowDB;
                    }
                }));
    }

    public void getPlaceByLatAndLng(String location) {
        subscription.add(DataBaseManager.getManager().getPlaceByLatAndLng(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaiduMapDB>() {
                    @Override
                    public void onCompleted() {
                        event.onSuccess(baiduMapData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        event.onError("百度地图城市数据请求异常！");
                    }

                    @Override
                    public void onNext(BaiduMapDB baiduMapDB) {
                        baiduMapData = baiduMapDB;
                    }
                }));
    }

    public void storageDataBase(Object object) {
        ActivityCollector.cachedThreadPool.execute(new HomePagerRunnable(this, object));
    }

    private static class HomePagerRunnable implements Runnable {
        private WeakReference<HomePagerPresenter> referencePresenter;
        private WeakReference<Object> referenceData;

        public HomePagerRunnable(HomePagerPresenter presenter, Object data) {
            referencePresenter = new WeakReference<>(presenter);
            referenceData = new WeakReference<>(data);
        }

        @Override
        public void run() {
            Object object = referenceData.get();
            if (object instanceof DriverProcessDB) {
                DataSupport.deleteAll(DriverProcessEntity.class);
                List<DriverProcessEntity> entities = ((DriverProcessDB) object).getEntities();
                if (entities != null && entities.size() > 0) {
                    referencePresenter.get().driverProcessSize = entities.size();
                    if (ActivityConstantEntity.examFirstRequestNet) {
                        ActivityConstantEntity.examFirstRequestNet = false;
                        DataSupport.deleteAll(ExamCheatsEntity.class);
                    }
                    for (DriverProcessEntity entity : entities) {
                        referencePresenter.get().requestExamCheats(String.valueOf(entity.getProcess_order()));
                        entity.save();
                    }
                }
                referencePresenter.get().referenceActivity.get().refreshExamCheatsTitlePart();
            } else if (object instanceof ExamCheatsDB) {
                List<ExamCheatsEntity> entities = ((ExamCheatsDB) object).getExamCheats();
                if (entities != null && entities.size() > 0) {
                    for (ExamCheatsEntity entity : entities) {
                        int rowNum = entity.updateAll("netId=?", String.valueOf(entity.getNetId()));
                        if (rowNum <= 0) {
                            entity.save();
                        }
                    }
                }
                synchronized (HomePagerRunnable.class) {
                    --referencePresenter.get().driverProcessSize;
                    if (referencePresenter.get().driverProcessSize == 0) {
                        referencePresenter.get().referenceActivity.get().refreshExamCheatsContentPart();
                    }
                }
            } else if (object instanceof HelpDB) {
                DataSupport.deleteAll(HelpEntity.class);
                List<HelpEntity> entities = ((HelpDB) object).getHelp();
                if (entities != null && entities.size() > 0) {
                    for (HelpEntity entity : entities) {
                        entity.save();
                    }
                }
            } else if (object instanceof MessageCenterDB) {
                DataSupport.deleteAll(MessageCenterEntity.class);
                boolean isShow = false;
                List<MessageCenterEntity> entities = ((MessageCenterDB) object).getMessage_center();
                if (entities != null && entities.size() > 0) {
                    for (MessageCenterEntity entity : entities) {
                        entity.save();
                        if(entity.getIsOpen() == 0) {
                            isShow = true;
                        }
                    }
                }
                referencePresenter.get().referenceActivity.get().setMessagePointerShow(isShow);
            } else if (object instanceof PaymentWayDB) {
                DataSupport.deleteAll(PaymentWayEntity.class);
                List<PaymentWayEntity> entities = ((PaymentWayDB) object).getPayment_way();
                if (entities != null && entities.size() > 0) {
                    for (PaymentWayEntity entity : entities) {
                        entity.save();
                    }
                }
            } else if (object instanceof CompleteOrderDB) {
                DataSupport.deleteAll(CompleteOrderEntity.class);
                List<CompleteOrderEntity> entities = ((CompleteOrderDB) object).getComplete_orders();
                if (entities != null && entities.size() > 0) {
                    for (CompleteOrderEntity entity : entities) {
                        entity.save();
                    }
                }
            } else if (object instanceof DriverShowDB) {
                DataSupport.deleteAll(DriverEntity.class);
                List<DriverEntity> entities = ((DriverShowDB) object).getEntities();
                if (entities != null && entities.size() > 0) {
                    for (DriverEntity entity : entities) {
                        entity.save();
                    }
                    referencePresenter.get().referenceActivity.get().refreshOrderDriverContentPart();
                }
            }
        }
    }
}
