package com.android.leezp.learncartrainproject.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.leezp.learncartrainproject.GlideApp;
import com.android.leezp.learncartrainproject.R;
import com.android.leezp.learncartrainproject.adapter.HomePagerExpandAdapter;
import com.android.leezp.learncartrainproject.entities.DriverEntity;
import com.android.leezp.learncartrainproject.entities.PublishOrderEntity;
import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.net.data.NetPublishOrderData;
import com.android.leezp.learncartrainproject.presenter.HomePagerPresenter;
import com.android.leezp.learncartrainproject.utils.ProjectConstant;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePagerActivity extends AppCompatActivity implements NetEvent, View.OnClickListener, View.OnTouchListener {
    private DrawerLayout menuDrawer;
    private ImageView headPartMenu;
    private ImageView headPartAdd;
    private ExpandableListView orderContainer;
    private LinearLayout addOrderPart;
    private TextView addOrderPartDate;
    private TextView addOrderPartBeginTime;
    private TextView addOrderPartEndTime;
    private TextView addOrderPartConfirm;
    private ImageView menuPartHead;
    private TextView menuPartName;
    private TextView menuPartComplete;
    private TextView menuPartMessage;
    private TextView menuPartHelp;
    private DriverEntity driverEntity;
    private HomePagerExpandAdapter adapter;
    private HomePagerPresenter presenter;
    private List<String> dateData = null;
    private Map<String, List<String>> timeSlotData = null;
    private HomePagerHandler handler;
    private float startX;
    private float startY;
    private boolean isStartTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pager);
        menuDrawer = findViewById(R.id.activity_home_pager_menu_drawer);
        headPartMenu = findViewById(R.id.activity_home_pager_head_part_menu);
        headPartAdd = findViewById(R.id.activity_home_pager_head_part_add);
        orderContainer = findViewById(R.id.activity_home_pager_order_container);
        addOrderPart = findViewById(R.id.activity_home_pager_add_order_part);
        addOrderPartDate = findViewById(R.id.activity_home_pager_add_order_part_date);
        addOrderPartBeginTime = findViewById(R.id.activity_home_pager_add_order_part_begin_time);
        addOrderPartEndTime = findViewById(R.id.activity_home_pager_add_order_part_end_time);
        addOrderPartConfirm = findViewById(R.id.activity_home_pager_add_order_part_confirm);
        menuPartHead = findViewById(R.id.activity_home_pager_menu_part_head);
        menuPartName = findViewById(R.id.activity_home_pager_menu_part_name);
        menuPartComplete = findViewById(R.id.activity_home_pager_menu_part_complete);
        menuPartMessage = findViewById(R.id.activity_home_pager_menu_part_message);
        menuPartHelp = findViewById(R.id.activity_home_pager_menu_part_help);

        presenter = new HomePagerPresenter();
        presenter.onCreate();
        presenter.attachEvent(this);

        handler = new HomePagerHandler(this);

        adapter = new HomePagerExpandAdapter(dateData, timeSlotData);
        orderContainer.setAdapter(adapter);

        Intent intent = getIntent();
        loadData(intent);

        headPartAdd.setOnClickListener(this);
        addOrderPart.setOnTouchListener(this);
        addOrderPartDate.setOnClickListener(this);
        addOrderPartBeginTime.setOnClickListener(this);
        addOrderPartEndTime.setOnClickListener(this);
        addOrderPartConfirm.setOnClickListener(this);
        headPartMenu.setOnClickListener(this);
        menuPartName.setOnClickListener(this);
        menuPartHead.setOnClickListener(this);
        menuPartHelp.setOnClickListener(this);
        menuPartMessage.setOnClickListener(this);
        menuPartComplete.setOnClickListener(this);
    }

    /**
     * 销毁相关数据
     */
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    /**
     * 初始化本页面的数据
     */
    private void loadData(Intent intent) {
        boolean fromLoginOrRegister = intent.getBooleanExtra("fromLoginOrRegister", false);
        Serializable serializable = intent.getSerializableExtra("driver");
        if (serializable != null) {
            driverEntity = ((DriverEntity) serializable);
            menuPartName.setText(driverEntity.getName());
            loadNetImage(ProjectConstant.SERVER_ADDRESS + driverEntity.getHead_url(), R.color.color_white, menuPartHead);
            if (fromLoginOrRegister) {
                presenter.getPublishOrderByDriverID(String.valueOf(driverEntity.getId()));
            }
        } else {
            showMessage("错误打开本页面，请重新登录！");
            LoginOrRegisterActivity.startLoginOrRegisterActivity(this);
        }
    }

    /**
     * 启动本页面并将教练实体传递过来的方法
     *
     * @param activity **启动本页面的Activity**
     * @param entity   **教练实体**
     */
    public static void startHomePager(Activity activity, DriverEntity entity, boolean fromLoginOrRegister) {
        Intent intent = new Intent(activity, HomePagerActivity.class);
        intent.putExtra("driver", entity);
        intent.putExtra("fromLoginOrRegister", fromLoginOrRegister);
        activity.startActivity(intent);
    }


    /**
     * 输出一些提示信息
     *
     * @param message **传入需要显示的消息**
     */
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 数据请求成功进行相关处理
     *
     * @param object **后台返回的数据对象**
     */
    @Override
    public void onSuccess(Object object) {
        if (object instanceof NetPublishOrderData) {
            NetPublishOrderData orderData = (NetPublishOrderData) object;
            if (orderData.getState() == 1003 && orderData.getEntities() != null) {
                presenter.analyzeObject(orderData);
            } else if (orderData.getState() == 1011) {
                addOrderPart.setVisibility(View.GONE);
                presenter.getPublishOrderByDriverID(String.valueOf(driverEntity.getId()));
            } else {
                showMessage(orderData.getMessage());
            }
        } else {
            showMessage("数据解析有误！");
        }
    }

    /**
     * 数据请求失败调用此方法进行显示消息
     *
     * @param message 消息
     */
    @Override
    public void onError(String message) {
        showMessage(message);
    }

    /**
     * 刷新发布订单的内容
     */
    public void refreshPublishOrderContainer(List<String> dateData, Map<String, List<String>> timeSlotData) {
        this.dateData = dateData;
        this.timeSlotData = timeSlotData;
        Message message = Message.obtain();
        message.what = 0;
        handler.sendMessage(message);
    }

    /**
     * 本页面控件的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_home_pager_head_part_add:
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_in);
                addOrderPart.startAnimation(animation);
                addOrderPart.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_home_pager_add_order_part_date:
                showDateDialog();
                break;
            case R.id.activity_home_pager_add_order_part_begin_time:
                isStartTime = true;
                showTimeDialog();
                break;
            case R.id.activity_home_pager_add_order_part_end_time:
                isStartTime = false;
                showTimeDialog();
                break;
            case R.id.activity_home_pager_add_order_part_confirm:
                String date = addOrderPartDate.getText().toString();
                String beginTime = addOrderPartBeginTime.getText().toString();
                String endTime = addOrderPartEndTime.getText().toString();
                if (driverEntity == null) {
                    showMessage("未登录，请登录之后再发布订单");
                    return;
                }
                if (!isRightDateTime(date, beginTime, endTime)) {
                    showMessage("未选择对应的日期以及时间");
                    return;
                }
                PublishOrderEntity orderEntity = new PublishOrderEntity(0, driverEntity.getId(), date + " " + beginTime, date + " " + endTime);
                Map<String, String> params = new HashMap<>();
                params.put("role", "1");
                params.put("requestCode", "1");
                params.put("id", String.valueOf(driverEntity.getId()));
                params.put("phone", driverEntity.getPhone());
                params.put("password", driverEntity.getPassword());
                params.put("bookOrder", new Gson().toJson(orderEntity));
                presenter.publishOrder(params);
                break;
            case R.id.activity_home_pager_head_part_menu:
                menuDrawer.openDrawer(Gravity.START);
                break;
            case R.id.activity_home_pager_menu_part_name:
                LoginOrRegisterActivity.startLoginOrRegisterActivity(this);
                break;
            case R.id.activity_home_pager_menu_part_head:
                TrainInformationActivity.startTrainInformationActivity(this, driverEntity, false);
                break;
            case R.id.activity_home_pager_menu_part_help:
                HelpActivity.startHelpActivity(this, driverEntity);
                break;
            case R.id.activity_home_pager_menu_part_message:
                MessageCenterActivity.startMessageCenterActivity(this, driverEntity);
                break;
            case R.id.activity_home_pager_menu_part_complete:
                BookSuccessOrderActivity.startBookSuccessOrderActivity(this, driverEntity);
                break;
        }
    }

    /**
     * 判断是否是正确的时间
     *
     * @param date
     * @param beginTime
     * @param endTime
     * @return
     */
    private boolean isRightDateTime(String date, String beginTime, String endTime) {
        if (date.equals("请选择日期")) {
            return false;
        }
        if (beginTime.equals("请选择开始时间")) {
            return false;
        }
        if (endTime.equals("请选择结束时间")) {
            return false;
        }
        return true;
    }

    /**
     * 点击选择时间按钮，弹出日期对话框
     */
    private void showTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (isStartTime) {
                    addOrderPartBeginTime.setText(hourOfDay + ":" + minute + ":00");
                } else {
                    addOrderPartEndTime.setText(hourOfDay + ":" + minute + ":00");
                }
            }
        };
        new TimePickerDialog(this, TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, hour, minute, true).show();
    }

    /**
     * 点击选择日期按钮，弹出日期对话框
     */
    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                addOrderPartDate.setText(year + "-" + (++month) + "-" + dayOfMonth);
            }
        };
        DatePickerDialog pickerDialog = new DatePickerDialog(this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, listener, year, month, day);
        pickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() - 1000);
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        pickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        pickerDialog.show();
    }

    /**
     * 添加订单的触摸事件处理
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            if (Math.abs(y - startY) < 20 && x - startX < 20) {
                addOrderPart.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_out);
                addOrderPart.startAnimation(animation);
                return true;
            }
        }
        return false;
    }

    /**
     * 其他页面重新跳回本页面调用此方法,判断是否是由登录与注册页面跳转过来，如果是则重新刷新教练的数据
     *
     * @param intent **其他页面传递过来的参数**
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        loadData(intent);
    }

    private void loadNetImage(String url, int errorResId, ImageView view) {
        GlideApp.with(this)
                .load(url)
                .placeholder(R.drawable.image_loading_black)
                .error(errorResId)
                .into(view);
    }

    /**
     * 其他线程更新UI
     */
    private static class HomePagerHandler extends Handler {
        private WeakReference<HomePagerActivity> referenceActivity;

        public HomePagerHandler(HomePagerActivity activity) {
            referenceActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    referenceActivity.get().adapter.notifyDataChange(referenceActivity.get().dateData, referenceActivity.get().timeSlotData);
                    break;
            }
        }
    }
}
