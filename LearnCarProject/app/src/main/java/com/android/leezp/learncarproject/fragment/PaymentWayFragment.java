package com.android.leezp.learncarproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.activity.PersonalMenuActivity;
import com.android.leezp.learncarproject.adapter.PaymentWayAdapter;
import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.android.leezp.learncarproject.entity.PaymentTypeConstantEntity;
import com.android.leezp.learncarproject.entity.PaymentWayEntity;
import com.android.leezp.learncarproject.entity.StudentEntity;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;
import com.android.leezp.learncarproject.utils.listener.OnItemLongClickListener;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Leezp on 2018/4/2.
 */

public class PaymentWayFragment extends Fragment implements OnItemClickListener, View.OnTouchListener, View.OnClickListener, OnItemLongClickListener {

    private View view;
    private RecyclerView paymentRecycler;
    private PaymentWayAdapter adapter;
    private LinearLayout addWays;
    private TextView addAlipay;
    private TextView addWechat;
    private TextView addBuild;
    private TextView addAgriculture;
    private TextView addBusiness;
    private float startX;
    private float startY;
    private List<PaymentWayEntity> data = null;
    private PaymentWayHandler handler;
    private SharedPreferences preferences;
    private StudentEntity studentEntity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_payment_way, container, false);
        paymentRecycler = ((RecyclerView) view.findViewById(R.id.fragment_payment_way_recycler));
        addWays = ((LinearLayout) view.findViewById(R.id.fragment_payment_way_add_way));
        addAlipay = ((TextView) view.findViewById(R.id.fragment_payment_way_add_alipay));
        addWechat = ((TextView) view.findViewById(R.id.fragment_payment_way_add_wechat));
        addBuild = ((TextView) view.findViewById(R.id.fragment_payment_way_add_build));
        addAgriculture = ((TextView) view.findViewById(R.id.fragment_payment_way_add_agriculture));
        addBusiness = ((TextView) view.findViewById(R.id.fragment_payment_way_add_business));

        paymentRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        paymentRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new PaymentWayAdapter(data);
        //给RecyclerView的Adapter添加子控件点击事件
        adapter.addOnItemClickListener(this);
        adapter.addOnItemLongClickListener(this);
        paymentRecycler.setAdapter(adapter);

        addWays.setOnTouchListener(this);
        addAlipay.setOnClickListener(this);
        addWechat.setOnClickListener(this);
        addBuild.setOnClickListener(this);
        addAgriculture.setOnClickListener(this);
        addBusiness.setOnClickListener(this);

        /**
         * 初始化一些数据
         */
        initVariables();

        return view;
    }

    private void initVariables() {
        handler = new PaymentWayHandler(this);
        preferences = getActivity().getSharedPreferences(ActivityConstantEntity.personalInformation_sharePreferences_name, Context.MODE_PRIVATE);
        String information = preferences.getString("personalInformation", null);
        if (information != null) {
            studentEntity = new Gson().fromJson(information, StudentEntity.class);
            ActivityCollector.cachedThreadPool.execute(new PaymentWayRunnable(this, 0, 0, null));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position == 0) {
            if (studentEntity == null) {
                showMessage("用户未登录，无法添加支付方式！");
            } else {
                if (addWays.getVisibility() == View.GONE) {
                    showAddWays();
                }
            }
        } else if (view.getId() == R.id.fragment_payment_way_item_way_remove) {
            PaymentWayEntity entity = ((PaymentWayEntity) ((View) view.getParent()).getTag());
            ActivityCollector.cachedThreadPool.execute(new PaymentWayRunnable(this, 1, entity.getNetId(), null));
            adapter.removeDataChange(entity);
        } else {
            Toast.makeText(getContext(), "还未开启相关功能！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            startX = motionEvent.getX();
            startY = motionEvent.getY();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (Math.abs(startX - x) < 20 && startY - y < 20 && addWays.getVisibility() == View.VISIBLE) {
                closeAddWays();
            }
            return true;
        }
        return false;
    }

    private void showAddWays() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.payment_way_in);
        addWays.startAnimation(animation);
        addWays.setVisibility(View.VISIBLE);
    }

    private void closeAddWays() {
        addWays.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.payment_way_out);
        addWays.startAnimation(animation);
    }

    @Override
    public void onClick(View view) {
        PaymentWayEntity entity = null;
        switch (view.getId()) {
            case R.id.fragment_payment_way_add_alipay:
                entity = new PaymentWayEntity(0, studentEntity.getStudentId(), PaymentTypeConstantEntity.ALIPAY, PaymentTypeConstantEntity.ALIPAY_STR);
                ActivityCollector.cachedThreadPool.execute(new PaymentWayRunnable(this, 2, 0, entity));
                break;
            case R.id.fragment_payment_way_add_wechat:
                entity = new PaymentWayEntity(0, studentEntity.getStudentId(), PaymentTypeConstantEntity.WECHAT, PaymentTypeConstantEntity.WECHAT_STR);
                ActivityCollector.cachedThreadPool.execute(new PaymentWayRunnable(this, 2, 0, entity));
                break;
            case R.id.fragment_payment_way_add_build:
                entity = new PaymentWayEntity(0, studentEntity.getStudentId(), PaymentTypeConstantEntity.BUILD, PaymentTypeConstantEntity.BUILD_STR);
                ActivityCollector.cachedThreadPool.execute(new PaymentWayRunnable(this, 2, 0, entity));
                break;
            case R.id.fragment_payment_way_add_agriculture:
                entity = new PaymentWayEntity(0, studentEntity.getStudentId(), PaymentTypeConstantEntity.AGRICULTURE, PaymentTypeConstantEntity.AGRICULTURE_STR);
                ActivityCollector.cachedThreadPool.execute(new PaymentWayRunnable(this, 2, 0, entity));
                break;
            case R.id.fragment_payment_way_add_business:
                entity = new PaymentWayEntity(0, studentEntity.getStudentId(), PaymentTypeConstantEntity.BUSINESS, PaymentTypeConstantEntity.BUSINESS_STR);
                ActivityCollector.cachedThreadPool.execute(new PaymentWayRunnable(this, 2, 0, entity));
                break;
        }
        addWays.setVisibility(View.GONE);
    }

    @Override
    public void onItemLongClick(View view) {
        View child = ((RelativeLayout) view).getChildAt(2);
        if (child.getVisibility() == View.GONE) {
            ((PaymentWayEntity) view.getTag()).setShowRemove(true);
            child.setVisibility(View.VISIBLE);
        } else {
            ((PaymentWayEntity) view.getTag()).setShowRemove(false);
            child.setVisibility(View.GONE);
        }
    }

    private static class PaymentWayRunnable implements Runnable {
        private WeakReference<PaymentWayFragment> referenceFragment;
        private WeakReference<PaymentWayEntity> referenceEntity;
        private int operateWay;
        private int netId;

        public PaymentWayRunnable(PaymentWayFragment fragment, int operateWay, int nettId, PaymentWayEntity entity) {
            referenceFragment = new WeakReference<>(fragment);
            referenceEntity = new WeakReference<>(entity);
            this.operateWay = operateWay;
            this.netId = nettId;
        }

        @Override
        public void run() {
            switch (operateWay) {
                case 0:
                    List<PaymentWayEntity> entities = DataSupport.findAll(PaymentWayEntity.class);
                    referenceFragment.get().data = entities;
                    Message message = Message.obtain();
                    message.what = 0;
                    referenceFragment.get().handler.sendMessage(message);
                    break;
                case 1:
                    int rowNum = DataSupport.deleteAll(PaymentWayEntity.class, "netId=?", String.valueOf(netId));
                    if (rowNum > 0) {
                        ((PersonalMenuActivity) referenceFragment.get().getActivity()).removePaymentWay(netId);
                    }
                    break;
                case 2:
                    List<PaymentWayEntity> paymentWayEntities = DataSupport.findAll(PaymentWayEntity.class);
                    if (paymentWayEntities != null && paymentWayEntities.size() >= 5) {
                        Message obtain = Message.obtain();
                        obtain.what = 1;
                        referenceFragment.get().handler.sendMessage(obtain);
                    } else {
                        ((PersonalMenuActivity) referenceFragment.get().getActivity()).addPaymentWay(referenceEntity.get());
                    }
                    break;
            }
        }
    }

    private static class PaymentWayHandler extends Handler {
        private WeakReference<PaymentWayFragment> reference = null;

        public PaymentWayHandler(PaymentWayFragment fragment) {
            reference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    reference.get().adapter.notifyDataChange(reference.get().data);
                    break;
                case 1:
                    reference.get().showMessage("支付方式已达上限，请删除一个，在进行添加！");
                    break;
            }
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void refreshPaymentWayRecycler() {
        ActivityCollector.cachedThreadPool.execute(new PaymentWayRunnable(this, 0, 0, null));
    }

}
