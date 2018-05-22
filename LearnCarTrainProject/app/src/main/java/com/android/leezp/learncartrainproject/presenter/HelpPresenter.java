package com.android.leezp.learncartrainproject.presenter;

import com.android.leezp.learncartrainproject.MyApplication;
import com.android.leezp.learncartrainproject.activities.HelpActivity;
import com.android.leezp.learncartrainproject.entities.HelpEntity;
import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.interfaces.Presenter;
import com.android.leezp.learncartrainproject.net.DataBaseManager;
import com.android.leezp.learncartrainproject.net.data.NetHelpData;
import com.android.leezp.learncartrainproject.utils.ProjectConstant;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class HelpPresenter implements Presenter {
    private CompositeSubscription subscription;
    private NetEvent event;
    private NetHelpData helpData;

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

    public void showAllHelpList() {
        if (ProjectConstant.FIRST_OPEN_HELP_PAGER) {
            ProjectConstant.FIRST_OPEN_HELP_PAGER = false;
            getApplicationHelp();
        } else {
            MyApplication.cachedThreadPool.execute(new HelpRunnable(this));
        }
    }

    private void getApplicationHelp() {
        subscription.add(DataBaseManager.getManager().getApplicationHelp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NetHelpData>() {
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
                    public void onNext(NetHelpData netHelpData) {
                        helpData = netHelpData;
                    }
                }));
    }

    public void storageApplicationHelp(NetHelpData data) {
        MyApplication.cachedThreadPool.execute(new HelpRunnable(this, data));
    }

    private static class HelpRunnable implements Runnable {
        private WeakReference<HelpPresenter> referencePresenter;
        private WeakReference<Object> referenceObject;

        public HelpRunnable(HelpPresenter presenter) {
            referencePresenter = new WeakReference<>(presenter);
        }

        public HelpRunnable(HelpPresenter presenter, Object object) {
            referencePresenter = new WeakReference<>(presenter);
            referenceObject = new WeakReference<>(object);
        }

        @Override
        public void run() {
            if (referenceObject == null) {
                List<HelpEntity> entities = DataSupport.findAll(HelpEntity.class);
                ((HelpActivity) referencePresenter.get().event).refreshHelpData(entities);
            } else {
                NetHelpData helpData = (NetHelpData) referenceObject.get();
                List<HelpEntity> entities = helpData.getHelp();
                DataSupport.deleteAll(HelpEntity.class);
                if (entities != null && entities.size() > 0) {
                    for (HelpEntity entity : entities) {
                        entity.save();
                    }
                }
                MyApplication.cachedThreadPool.execute(new HelpRunnable(referencePresenter.get()));
            }
        }
    }
}
