package com.android.leezp.learncarproject.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leezp.learncarlib.GlideApp;
import com.android.leezp.learncarlib.activity.BaseFragmentActivity;
import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.adapter.HomePagerAdapter;
import com.android.leezp.learncarproject.background.HomePagerService;
import com.android.leezp.learncarproject.background.NetworkBroadcastReceiver;
import com.android.leezp.learncarproject.db.BaiduMapDB;
import com.android.leezp.learncarproject.db.CompleteOrderDB;
import com.android.leezp.learncarproject.db.DriverProcessDB;
import com.android.leezp.learncarproject.db.DriverShowDB;
import com.android.leezp.learncarproject.db.ExamCheatsDB;
import com.android.leezp.learncarproject.db.HelpDB;
import com.android.leezp.learncarproject.db.LoginDB;
import com.android.leezp.learncarproject.db.MessageCenterDB;
import com.android.leezp.learncarproject.db.PaymentWayDB;
import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.android.leezp.learncarproject.entity.StudentEntity;
import com.android.leezp.learncarproject.fragment.DriverProcessFragment;
import com.android.leezp.learncarproject.fragment.ExamCheatsFragment;
import com.android.leezp.learncarproject.fragment.OrderDriveFragment;
import com.android.leezp.learncarproject.interfaces.NetAvailableInterface;
import com.android.leezp.learncarproject.presenter.HomePagerPresenter;
import com.android.leezp.learncarproject.utils.ServiceConnection.HomePagerServiceConnection;
import com.android.leezp.learncarproject.utils.event.NetEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leezp on 2018/4/1.
 */

public class HomePagerActivity extends BaseFragmentActivity implements View.OnClickListener, View.OnTouchListener, ViewPager.OnPageChangeListener {

    private final int OrderDriver = 30001;
    private final int ExamCheats = 30002;
    private final int DriverProcess = 30003;

    private ImageView personalMenuBtn;
    private TextView personalName;
    private TextView personalPayment;
    private TextView personalTrail;
    private TextView personalMessage;
    private TextView personalHelp;
    private FrameLayout menuPager;
    private float startX;
    private float startY;
    private ViewPager container;
    private List<Fragment> data;
    private HomePagerAdapter adapter;
    private TextView orderDriverBtn;
    private TextView examCheatsBtn;
    private TextView driverProcess;
    private TextView title;
    private FrameLayout messageBtn;

    private HomePagerPresenter presenter;
    private NetEvent event = new NetEvent() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof LoginDB) {
                LoginDB loginDB = (LoginDB) object;
                switch (loginDB.getState()) {
                    case 1001:
                        studentEntity = loginDB.getStudent();

                        //登陆成功后刷新界面信息
                        personalHead.setClickable(true);
                        processFragment.setTitleClickable();
                        personalName.setText(studentEntity.getName());
                        requestHeadImage(ActivityConstantEntity.SERVER_ADDRESS + studentEntity.getHeadUrl());

                        //插入数据
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("personalInformation", new Gson().toJson(studentEntity));
                        editor.apply();

                        //开启定时获取数据服务
                        startTimingService();

                        break;
                    default:
                        personalHead.setClickable(false);
                        showMessage(loginDB.getMessage());
                }
            } else if (object instanceof DriverProcessDB) {
                DriverProcessDB processDB = ((DriverProcessDB) object);
                switch (processDB.getState()) {
                    case 1001:
                        presenter.storageDataBase(processDB);
                        break;
                    default:
                        showMessage(processDB.getMessage());
                }
            } else if (object instanceof ExamCheatsDB) {
                ExamCheatsDB examCheatsDB = (ExamCheatsDB) object;
                switch (examCheatsDB.getState()) {
                    case 1002:
                        presenter.storageDataBase(examCheatsDB);
                        break;
                    case 1000:
                        showMessage(examCheatsDB.getMessage());
                        break;
                }
            } else if (object instanceof HelpDB) {
                HelpDB helpDB = (HelpDB) object;
                switch (helpDB.getState()) {
                    case 1001:
                        presenter.storageDataBase(helpDB);
                        break;
                    default:
                        showMessage(helpDB.getMessage());
                }
            } else if (object instanceof MessageCenterDB) {
                MessageCenterDB centerDB = (MessageCenterDB) object;
                switch (centerDB.getState()) {
                    case 1002:
                        presenter.storageDataBase(centerDB);
                        break;
                    default:
                        showMessage(centerDB.getMessage());
                }
            } else if (object instanceof PaymentWayDB) {
                PaymentWayDB paymentWayDB = (PaymentWayDB) object;
                switch (paymentWayDB.getState()) {
                    case 1002:
                        presenter.storageDataBase(paymentWayDB);
                        break;
                    default:
                        showMessage(paymentWayDB.getMessage());
                }
            } else if (object instanceof CompleteOrderDB) {
                CompleteOrderDB completeOrderDB = (CompleteOrderDB) object;
                switch (completeOrderDB.getState()) {
                    case 1002:
                        presenter.storageDataBase(completeOrderDB);
                        break;
                    default:
                        showMessage(completeOrderDB.getMessage());
                }
            } else if (object instanceof DriverShowDB) {
                DriverShowDB driverShowDB = (DriverShowDB) object;
                switch (driverShowDB.getState()) {
                    case 1001:
                        presenter.storageDataBase(driverShowDB);
                        break;
                    default:
                        showMessage(driverShowDB.getMessage());
                }
            } else if (object instanceof BaiduMapDB) {
                BaiduMapDB mapDB = (BaiduMapDB) object;
                if (mapDB.result.addressComponent.city != null) {
                    placeCity = mapDB.result.addressComponent.city;
                    if (firstOpen) {
                        firstOpen = false;
                        getDriverInformation();
                    }
                } else {
                    showMessage("地图信息解析有误！");
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
    private SharedPreferences preferences;
    private StudentEntity studentEntity;
    private DriverProcessFragment processFragment;
    private ExamCheatsFragment cheatsFragment;
    private OrderDriveFragment driveFragment;
    private ImageView personalHead;
    private LocationManager locationManager;
    private NetworkBroadcastReceiver networkBroadcastReceiver =  new NetworkBroadcastReceiver(new NetAvailableInterface() {
        @Override
        public void netAvailable() {
            getLocation();
        }
    });
    private IntentFilter intentFilter;
    private ImageView messagePointer;
    private HomePagerServiceConnection serviceConnection;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (presenter != null && location != null) {
                presenter.getPlaceByLatAndLng(location.getLatitude() + "," + location.getLongitude());
            } else if (location == null) {
                showMessage("该区域无法使用GPS与网络定位");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
            showMessage("GPS与网络未打开，无法定位");
        }
    };
    private String placeCity;
    private boolean firstOpen = true;

    @Override
    protected void initVariables() {
        ActivityCollector.addActivity(this);
        presenter = new HomePagerPresenter(this);
        presenter.onCreate();
        presenter.attachEvent(event);

        preferences = getSharedPreferences(ActivityConstantEntity.personalInformation_sharePreferences_name, MODE_PRIVATE);
        String information = preferences.getString("personalInformation", null);
        if (information != null) {
            studentEntity = new Gson().fromJson(information, StudentEntity.class);
        }

        data = new ArrayList<>();
        driveFragment = new OrderDriveFragment();
        data.add(driveFragment);
        cheatsFragment = new ExamCheatsFragment();
        data.add(cheatsFragment);
        processFragment = new DriverProcessFragment();
        data.add(processFragment);

        locationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkBroadcastReceiver, intentFilter);

        serviceConnection = new HomePagerServiceConnection(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home_pager);
        menuPager = ((FrameLayout) findViewById(R.id.activity_home_pager_menu));
        personalMenuBtn = ((ImageView) findViewById(R.id.activity_home_pager_personal_information_menu));
        personalName = ((TextView) findViewById(R.id.activity_home_pager_personal_menu_name));
        personalPayment = ((TextView) findViewById(R.id.activity_home_pager_personal_menu_payment));
        personalTrail = ((TextView) findViewById(R.id.activity_home_pager_personal_menu_trail));
        personalMessage = ((TextView) findViewById(R.id.activity_home_pager_personal_menu_message));
        personalHelp = ((TextView) findViewById(R.id.activity_home_pager_personal_menu_help));
        container = ((ViewPager) findViewById(R.id.activity_home_pager_container));
        orderDriverBtn = ((TextView) findViewById(R.id.activity_home_pager_order_driver_btn));
        examCheatsBtn = ((TextView) findViewById(R.id.activity_home_pager_exam_cheats_btn));
        driverProcess = ((TextView) findViewById(R.id.activity_home_pager_driver_process_btn));
        title = ((TextView) findViewById(R.id.activity_home_pager_title));
        messageBtn = ((FrameLayout) findViewById(R.id.activity_home_pager_message));
        personalHead = ((ImageView) findViewById(R.id.activity_home_pager_personal_menu_head));
        messagePointer = ((ImageView) findViewById(R.id.activity_home_pager_message_pointer));

        adapter = new HomePagerAdapter(getSupportFragmentManager(), data);
        container.setAdapter(adapter);
        container.addOnPageChangeListener(this);

        menuPager.setOnTouchListener(this);
        personalMenuBtn.setOnClickListener(this);
        personalName.setOnClickListener(this);
        personalPayment.setOnClickListener(this);
        personalTrail.setOnClickListener(this);
        personalMessage.setOnClickListener(this);
        personalHelp.setOnClickListener(this);
        orderDriverBtn.setOnClickListener(this);
        examCheatsBtn.setOnClickListener(this);
        driverProcess.setOnClickListener(this);
        messageBtn.setOnClickListener(this);
        personalHead.setOnClickListener(this);

        personalHead.setClickable(false);
    }

    @Override
    protected void loadData() {
        //获取学车流程
        presenter.requestDriverProcess();
        //获取帮助
        presenter.getHelp();

        //判断用户是否已经登陆
        if (studentEntity != null) {
            presenter.requestUserLogined(studentEntity.getPhone(), studentEntity.getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        /**
         * 关闭定时获取数据服务
         */
        stopTimingService();
        unregisterReceiver(networkBroadcastReceiver);
        ActivityCollector.removeActivity(this);
    }

    /**
     * 启动HomeActivity时只需调用此方法，传入activity
     *
     * @param activity
     */
    public static void startHomePagerActivity(Activity activity) {
        Intent intent = new Intent(activity, HomePagerActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_home_pager_personal_information_menu:
                openPersonalMenu();
                break;
            case R.id.activity_home_pager_personal_menu_name:
                UserLoginActivity.startUserLoginActivity(this);
                menuPager.setVisibility(View.GONE);
                break;
            case R.id.activity_home_pager_personal_menu_payment:
                PersonalMenuActivity.startPersonalMenuActivity(this, gotoPersonMenuActivity(view), ActivityConstantEntity.personalMenuPaymentWay);
                break;
            case R.id.activity_home_pager_personal_menu_trail:
                PersonalMenuActivity.startPersonalMenuActivity(this, gotoPersonMenuActivity(view), ActivityConstantEntity.personalMenuDriverTrail);
                break;
            case R.id.activity_home_pager_personal_menu_message:
                PersonalMenuActivity.startPersonalMenuActivity(this, gotoPersonMenuActivity(view), ActivityConstantEntity.personalMenuMessageCenter);
                break;
            case R.id.activity_home_pager_personal_menu_help:
                PersonalMenuActivity.startPersonalMenuActivity(this, gotoPersonMenuActivity(view), ActivityConstantEntity.personalMenuHelp);
                break;
            case R.id.activity_home_pager_order_driver_btn:
                bottomButtonChange(OrderDriver);
                break;
            case R.id.activity_home_pager_driver_process_btn:
                bottomButtonChange(DriverProcess);
                break;
            case R.id.activity_home_pager_exam_cheats_btn:
                bottomButtonChange(ExamCheats);
                break;
            case R.id.activity_home_pager_message:
                PersonalMenuActivity.startPersonalMenuActivity(this, "消息中心", ActivityConstantEntity.personalMenuMessageCenter);
                break;
            case R.id.activity_home_pager_personal_menu_head:
                PersonalInformationActivity.startPersonalInformationActivity(this, "个人信息", ActivityConstantEntity.normalPersonalInformation);
                break;
        }
    }

    /**
     * 打开个人信息侧边栏
     */
    private void openPersonalMenu() {
        menuPager.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.personal_information_menu_in);
        menuPager.startAnimation(animation);
    }

    /**
     * 关闭个人信息侧边栏
     */
    private void closePersonMenu() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.personal_information_menu_out);
        menuPager.startAnimation(animation);
        menuPager.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            startX = motionEvent.getX();
            startY = motionEvent.getY();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (menuPager.getVisibility() == View.VISIBLE && x - startX <= 0 && Math.abs(startY - y) < 20) {
                closePersonMenu();
                return true;
            }
        }
        return false;
    }

    /**
     * 获取个人信息
     *
     * @return
     */
    public StudentEntity getStudentEntity() {
        return studentEntity;
    }

    private String gotoPersonMenuActivity(View view) {
        String str = ((TextView) view).getText().toString();
        menuPager.setVisibility(View.GONE);
        return str;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                bottomButtonChange(OrderDriver);
                break;
            case 1:
                bottomButtonChange(ExamCheats);
                break;
            case 2:
                bottomButtonChange(DriverProcess);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void bottomButtonChange(int type) {

        Drawable drawable = getResources().getDrawable(R.drawable.order_driver_btn);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        orderDriverBtn.setCompoundDrawables(null, drawable, null, null);
        orderDriverBtn.setTextColor(getResources().getColor(R.color.color_bottom_menu_gray));
        drawable = getResources().getDrawable(R.drawable.exam_cheats_btn);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        examCheatsBtn.setCompoundDrawables(null, drawable, null, null);
        examCheatsBtn.setTextColor(getResources().getColor(R.color.color_bottom_menu_gray));
        drawable = getResources().getDrawable(R.drawable.driver_process_btn);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        driverProcess.setCompoundDrawables(null, drawable, null, null);
        driverProcess.setTextColor(getResources().getColor(R.color.color_bottom_menu_gray));

        switch (type) {
            case OrderDriver:
                container.setCurrentItem(0);
                title.setText(orderDriverBtn.getText().toString());
                drawable = getResources().getDrawable(R.drawable.order_driver_click_btn);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                orderDriverBtn.setCompoundDrawables(null, drawable, null, null);
                orderDriverBtn.setTextColor(getResources().getColor(R.color.color_bottom_menu_focus_blue));
                break;
            case ExamCheats:
                container.setCurrentItem(1);
                title.setText(examCheatsBtn.getText().toString());
                drawable = getResources().getDrawable(R.drawable.exam_cheats_click_btn);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                examCheatsBtn.setCompoundDrawables(null, drawable, null, null);
                examCheatsBtn.setTextColor(getResources().getColor(R.color.color_bottom_menu_focus_blue));
                break;
            case DriverProcess:
                container.setCurrentItem(2);
                title.setText(driverProcess.getText().toString());
                drawable = getResources().getDrawable(R.drawable.driver_process_click_btn);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                driverProcess.setCompoundDrawables(null, drawable, null, null);
                driverProcess.setTextColor(getResources().getColor(R.color.color_bottom_menu_focus_blue));
                break;
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 其他界面返回本界面时，获取是否已经登陆
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String information = preferences.getString("personalInformation", null);
        if (information != null) {
            studentEntity = new Gson().fromJson(information, StudentEntity.class);
        }
        if (studentEntity != null) {
            personalHead.setClickable(true);
            requestHeadImage(ActivityConstantEntity.SERVER_ADDRESS + studentEntity.getHeadUrl());
            personalName.setText(studentEntity.getName());
            processFragment.setTitleClickable();
            /**
             * 开启定时获取数据服务
             */
            startTimingService();
        } else {
            personalHead.setClickable(false);
        }
    }

    /**
     * 加载头像的方法
     *
     * @param url 传入url地址
     */
    private void requestHeadImage(String url) {
        GlideApp.with(this)
                .load(url)
                .placeholder(R.drawable.image_loading_white)
                .error(R.drawable.personal_menu_head)
                .into(personalHead);
    }

    /**
     * 刷新考试秘籍界面标题信息
     */
    public void refreshExamCheatsTitlePart() {
        cheatsFragment.refreshTitlePart();
    }

    /**
     * 刷新考试秘籍界面内容信息
     */
    public void refreshExamCheatsContentPart() {
        cheatsFragment.refreshContentPart();
    }

    /**
     * 数据加载完毕后刷新学车订单数据
     */
    public void refreshOrderDriverContentPart() {
        driveFragment.refreshOrderDriverData();
    }


    /**
     * 用户登陆成功后需要请求的数据
     */
    public void userLoginSuccessRequest() {
        //消息中心的信息获取
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("code", "0");
        msgMap.put("role", "0");
        msgMap.put("id", String.valueOf(studentEntity.getStudentId()));
        msgMap.put("phone", studentEntity.getPhone());
        msgMap.put("password", studentEntity.getPassword());
        presenter.getMessageCenter(msgMap);

        // 已完工程的订单
        presenter.getCompleteOrders("0", "0", String.valueOf(studentEntity.getStudentId()), studentEntity.getPhone(), studentEntity.getPassword());

        // 支付方式获取
        Map<String, String> payMap = new HashMap<>();
        payMap.put("code", "0");
        payMap.put("id", String.valueOf(studentEntity.getStudentId()));
        payMap.put("phone", studentEntity.getPhone());
        payMap.put("password", studentEntity.getPassword());
        presenter.getPaymentWays(payMap);
    }

    /**
     * 获取手机地址
     */
    private void getLocation() {
        //API版本超过6.0需要动态申请权限
        if (Build.VERSION.SDK_INT >= 23) {
            //如果用户并没有同意该权限
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
        Location location = locationManager.getLastKnownLocation(getProvider());
        if (location != null) {
            presenter.getPlaceByLatAndLng(location.getLatitude() + "," + location.getLongitude());
        } else if (location == null) {
            locationManager.requestLocationUpdates(getProvider(), 1000, 1, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    getLocation();
                    showMessage("请打开定位权限，否则无法获取学车订单！");
                }
                break;
            default:
        }
    }

    /**
     * 消息中心是否存在未读消息进行界面设计
     */
    public void setMessagePointerShow(final boolean isShow) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    messagePointer.setVisibility(View.VISIBLE);
                } else {
                    messagePointer.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 开启定时获取数据服务
     */
    private void startTimingService() {
        Intent intent = new Intent(this, HomePagerService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 关闭定时获取数据服务
     */
    private void stopTimingService() {
        Intent intent = new Intent(this, HomePagerService.class);
        unbindService(serviceConnection);
        stopService(intent);
    }

    /**
     * @return 返回当前最合适地址定位方式
     */
    private String getProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return locationManager.getBestProvider(criteria, true);
    }

    /**
     * 获取教练信息
     */
    public void getDriverInformation() {
        if (placeCity != null) {
            presenter.findDriverOrderByPlace(placeCity);
        } else {
            showMessage("城市信息未获取到，请先打开网络与GPS");
        }
    }
}
