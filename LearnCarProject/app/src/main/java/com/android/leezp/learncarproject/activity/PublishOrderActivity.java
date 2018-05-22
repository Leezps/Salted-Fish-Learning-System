package com.android.leezp.learncarproject.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.leezp.learncarlib.activity.BaseActivity;
import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.adapter.PublishOrderBookAdapter;
import com.android.leezp.learncarproject.adapter.PublishOrderExpandleAdapter;
import com.android.leezp.learncarproject.background.PublishOrderService;
import com.android.leezp.learncarproject.db.CompleteOrderDB;
import com.android.leezp.learncarproject.db.PublishOrderDB;
import com.android.leezp.learncarproject.entity.PublishOrderEntity;
import com.android.leezp.learncarproject.entity.StudentEntity;
import com.android.leezp.learncarproject.presenter.PublishOrderPresenter;
import com.android.leezp.learncarproject.utils.ServiceConnection.PublishOrderServiceConnection;
import com.android.leezp.learncarproject.utils.event.NetEvent;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishOrderActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener, View.OnTouchListener {

    private ImageView back;
    private ExpandableListView container;
    private LinearLayout bookOrder;
    private TextView bookTimeSlot;
    private RecyclerView bookRecycler;
    private TextView bookBeginTime;
    private TextView bookEndTime;
    private TextView bookConfirm;
    private TextView bookCancel;
    private LinearLayout timePart;
    private TimePicker timeSelector;
    private PublishOrderPresenter presenter;
    private NetEvent event = new NetEvent() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof PublishOrderDB) {
                PublishOrderDB orderDB = (PublishOrderDB) object;
                switch (orderDB.getState()) {
                    case 1003:
                        presenter.analyzeObject(orderDB);
                        break;
                    case 1012:
                        showMessage(orderDB.getMessage());
                        bookOrder.setVisibility(View.GONE);
                        break;
                    default:
                        showMessage(orderDB.getMessage());
                }
            } else if (object instanceof CompleteOrderDB) {
                CompleteOrderDB completeOrderDB = (CompleteOrderDB) object;
                switch (completeOrderDB.getState()) {
                    case 1005:
                        presenter.analyzeObject(completeOrderDB);
                        break;
                    default:
                        showMessage(completeOrderDB.getMessage());
                }
            } else {
                showMessage("数据解析有误！");
            }
        }

        @Override
        public void onError(String message) {
            showMessage(message);
        }
    };
    private List<String> dateData = null;
    private Map<String, List<String>> timeSlotData = null;
    private Map<String, List<Integer>> publishOrderIdData = null;
    private List<String> completeOrderTimeSlotData = null;
    private PublishOrderExpandleAdapter adapter;
    private PublishOrderHandler handler;
    private float startX;
    private float startY;
    private PublishOrderBookAdapter bookAdapter;
    private boolean isStartTime = true;
    private StudentEntity studentEntity = null;
    private int driverID = 0;
    private PublishOrderServiceConnection serviceConnection;

    @Override
    protected void initVariables() {
        ActivityCollector.addActivity(this);

        presenter = new PublishOrderPresenter(this);
        presenter.onCreate();
        presenter.attachEvent(event);

        handler = new PublishOrderHandler(this);

        serviceConnection = new PublishOrderServiceConnection(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_publish_order);
        back = findViewById(R.id.activity_publish_order_back);
        container = findViewById(R.id.activity_publish_order_container);
        bookOrder = findViewById(R.id.activity_publish_order_book_order);
        bookTimeSlot = findViewById(R.id.activity_publish_order_book_order_time_slot);
        bookRecycler = findViewById(R.id.activity_publish_order_book_order_recycler);
        bookBeginTime = findViewById(R.id.activity_publish_order_book_order_begin_time);
        bookEndTime = findViewById(R.id.activity_publish_order_book_order_end_time);
        bookConfirm = findViewById(R.id.activity_publish_order_book_order_confirm);
        bookCancel = findViewById(R.id.activity_publish_order_book_order_cancel);
        timePart = findViewById(R.id.activity_publish_order_time_part);
        timeSelector = findViewById(R.id.activity_publish_order_time_part_selector);

        adapter = new PublishOrderExpandleAdapter(dateData, timeSlotData, publishOrderIdData);
        adapter.addOnItemClickListener(this);
        container.setAdapter(adapter);

        bookRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bookRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        bookAdapter = new PublishOrderBookAdapter(completeOrderTimeSlotData);
        bookRecycler.setAdapter(bookAdapter);

        back.setOnClickListener(this);
        bookBeginTime.setOnClickListener(this);
        bookEndTime.setOnClickListener(this);
        bookOrder.setOnTouchListener(this);
        timePart.setOnTouchListener(this);
        bookConfirm.setOnClickListener(this);
        bookCancel.setOnClickListener(this);

        timeSelector.setIs24HourView(true);
    }

    @Override
    protected void loadData() {
        Intent intent = getIntent();
        driverID = intent.getIntExtra("driverID", 0);
        if (driverID != 0) {
            getPublishOrder();
            startTimingService();
        }
        Serializable student = intent.getSerializableExtra("student");
        if (student != null) {
            studentEntity = ((StudentEntity) student);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 关闭定时刷新数据服务
         */
        stopTimingService();
        presenter.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public static void startPublishOrderActivity(Activity activity, int driverID, StudentEntity studentEntity) {
        Intent intent = new Intent(activity, PublishOrderActivity.class);
        intent.putExtra("driverID", driverID);
        intent.putExtra("student", studentEntity);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_publish_order_back:
                ActivityCollector.removeActivity(this);
                presenter.onDestroy();
                finish();
                break;
            case R.id.activity_publish_order_book_order_begin_time:
                isStartTime = true;
                timePart.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_publish_order_book_order_end_time:
                isStartTime = false;
                timePart.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_publish_order_book_order_confirm:
                requestBookOrder();
                break;
            case R.id.activity_publish_order_book_order_cancel:
                bookOrder.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 请求预约发布订单
     */
    private void requestBookOrder() {
        String beginTime_str = bookBeginTime.getText().toString();
        String endTime_str = bookEndTime.getText().toString();
        if (beginTime_str.equals("开始时间") || endTime_str.equals("结束时间")) {
            showMessage("请选择时间段");
            return;
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            if (timeFormat.parse(beginTime_str).compareTo(timeFormat.parse(endTime_str)) >= 0) {
                showMessage("时间段不存在，不要玩我！");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (studentEntity == null) {
            showMessage("用户不存在！请登录再预定");
            return;
        }
        Integer publishOrderId = (Integer) bookOrder.getTag(R.id.tag_first);
        String date_str = (String) bookOrder.getTag(R.id.tag_second);
        PublishOrderEntity entity = new PublishOrderEntity(publishOrderId, driverID, date_str + " " + beginTime_str, date_str + " " + endTime_str);
        Map<String, String> map = new HashMap<>();
        map.put("role", "0");
        map.put("requestCode", "1");
        map.put("id", String.valueOf(studentEntity.getStudentId()));
        map.put("phone", studentEntity.getPhone());
        map.put("password", studentEntity.getPassword());
        map.put("bookOrder", new Gson().toJson(entity));
        presenter.studentBookOrder(map);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Object object = view.getTag(R.id.tag_first);
        bookOrder.setTag(R.id.tag_first, object);
        bookOrder.setTag(R.id.tag_second, view.getTag(R.id.tag_second));
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_in);
        bookOrder.startAnimation(animation);
        bookOrder.setVisibility(View.VISIBLE);
        bookTimeSlot.setText("订单时间段：" + ((TextView) view).getText().toString());
        presenter.getCompOrderByPublishOrder("2", String.valueOf(object));
    }

    public void refreshPublishOrderContainer(List<String> dateData, Map<String, List<String>> timeSlotData, Map<String, List<Integer>> publishOrderIdData) {
        this.dateData = dateData;
        this.timeSlotData = timeSlotData;
        this.publishOrderIdData = publishOrderIdData;
        Message message = Message.obtain();
        message.what = 0;
        handler.sendMessage(message);
    }

    public void refreshCompleteOrderRecycler(List<String> completeOrderTimeSlotData) {
        this.completeOrderTimeSlotData = completeOrderTimeSlotData;
        Message message = Message.obtain();
        message.what = 1;
        handler.sendMessage(message);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            startX = motionEvent.getX();
            startY = motionEvent.getY();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (Math.abs(y - startY) < 20 && x - startX < 20) {
                switch (view.getId()) {
                    case R.id.activity_publish_order_book_order:
                        bookOrder.setVisibility(View.GONE);
                        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_out);
                        bookOrder.startAnimation(animation);
                        break;
                    case R.id.activity_publish_order_time_part:
                        if (isStartTime) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                bookBeginTime.setText(getTimeSelector23());
                            } else {
                                bookBeginTime.setText(getTimeSelector());
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= 23) {
                                bookEndTime.setText(getTimeSelector23());
                            } else {
                                bookEndTime.setText(getTimeSelector());
                            }
                        }
                        timePart.setVisibility(View.GONE);
                        Animation timeAnimation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_out);
                        timePart.startAnimation(timeAnimation);
                        break;
                }
                return true;
            }
        }
        return false;
    }

    @TargetApi(23)
    private String getTimeSelector23() {
        int hour = timeSelector.getHour();
        int minute = timeSelector.getMinute();
        return hour + ":" + minute + ":00";
    }

    private String getTimeSelector() {
        Integer hour = timeSelector.getCurrentHour();
        Integer minute = timeSelector.getCurrentMinute();
        return hour + ":" + minute + ":00";
    }

    /**
     * 获取发布订单
     */
    public void getPublishOrder() {
        if (driverID != 0) {
            presenter.getPublishOrderByDriverID("0", "0", String.valueOf(driverID));
        }
    }

    /**
     * 开启定时获取数据服务
     */
    private void startTimingService() {
        Intent intent = new Intent(this, PublishOrderService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 关闭定时获取数据服务
     */
    private void stopTimingService() {
        Intent intent = new Intent(this, PublishOrderService.class);
        unbindService(serviceConnection);
        stopService(intent);
    }

    private static class PublishOrderHandler extends Handler {
        private WeakReference<PublishOrderActivity> referenceActivity;

        public PublishOrderHandler(PublishOrderActivity activity) {
            referenceActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    referenceActivity.get().adapter.notifyDataChange(referenceActivity.get().dateData, referenceActivity.get().timeSlotData, referenceActivity.get().publishOrderIdData);
                    break;
                case 1:
                    referenceActivity.get().bookAdapter.notifyDataChange(referenceActivity.get().completeOrderTimeSlotData);
                    break;
            }
        }
    }
}
