package com.android.leezp.learncartrainproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leezp.learncartrainproject.R;
import com.android.leezp.learncartrainproject.entities.DriverEntity;
import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.net.data.NetDriverData;
import com.android.leezp.learncartrainproject.presenter.LoginOrRegisterPresenter;
import com.android.leezp.learncartrainproject.utils.ProjectConstant;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginOrRegisterActivity extends AppCompatActivity implements View.OnClickListener, NetEvent, View.OnTouchListener {
    private EditText loginPhone;
    private EditText loginPassword;
    private TextView loginBtn;
    private FloatingActionButton showEnrollBtn;
    private LinearLayout enrollPart;
    private EditText enrollPhone;
    private EditText enrollVerification;
    private TextView getEnrollVerification;
    private EditText enrollPassword;
    private EditText enrollConfirm;
    private TextView enrollBtn;
    private SharedPreferences preferences;
    private DriverEntity driverEntity;
    private LoginOrRegisterPresenter presenter;
    private float startX;
    private float startY;
    private LoginOrRegisterHandler handler;
    private String verificationBizID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        loginPhone = findViewById(R.id.activity_login_or_register_phone);
        loginPassword = findViewById(R.id.activity_login_or_register_password);
        loginBtn = findViewById(R.id.activity_login_or_register_land_btn);
        showEnrollBtn = findViewById(R.id.activity_login_or_register_enroll_btn);
        enrollPart = findViewById(R.id.activity_login_or_register_enroll_part);
        enrollPhone = findViewById(R.id.activity_login_or_register_enroll_part_phone);
        enrollVerification = findViewById(R.id.activity_login_or_register_enroll_part_verification);
        getEnrollVerification = findViewById(R.id.activity_login_or_register_enroll_part_get_verification);
        enrollPassword = findViewById(R.id.activity_login_or_register_enroll_part_password);
        enrollConfirm = findViewById(R.id.activity_login_or_register_enroll_part_confirm);
        enrollBtn = findViewById(R.id.activity_login_or_register_enroll_part_btn);

        presenter = new LoginOrRegisterPresenter();
        presenter.onCreate();
        presenter.attachEvent(this);
        handler = new LoginOrRegisterHandler(this);

        loadData();

        loginBtn.setOnClickListener(this);
        showEnrollBtn.setOnClickListener(this);
        getEnrollVerification.setOnClickListener(this);
        enrollPart.setOnTouchListener(this);
        enrollBtn.setOnClickListener(this);
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
     * 初始化，观察是否是最开始启动app进入此界面，如果是则判断是否已登录，登陆则直接进入主页界面
     */
    private void loadData() {
        Intent intent = getIntent();
        if (intent == null || intent.getBooleanExtra("noRefreshUser", true)) {
            preferences = getSharedPreferences(ProjectConstant.SHARED_PREFERENCES, MODE_PRIVATE);
            String information = preferences.getString(ProjectConstant.PERSONAL_INFORMATION, null);
            if (information != null) {
                driverEntity = new Gson().fromJson(information, DriverEntity.class);
                startHomePagerActivity();
            }
        }
    }

    /**
     * 用户登陆另外一个账号或注册新的用户的启动方法
     *
     * @param activity 启动本活动的活动
     */
    public static void startLoginOrRegisterActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginOrRegisterActivity.class);
        intent.putExtra("noRefreshUser", false);
        activity.startActivity(intent);
    }

    /**
     * 点击事件的实现
     *
     * @param v 点击的控件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login_or_register_land_btn:
                String phone = loginPhone.getText().toString();
                String password = loginPassword.getText().toString();
                if (isRightLoginInformation(phone, password)) {
                    presenter.loginDriver(phone, password);
                }
                break;
            case R.id.activity_login_or_register_enroll_btn:
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_in);
                enrollPart.startAnimation(animation);
                enrollPart.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_login_or_register_enroll_part_get_verification:
                String verPhone = enrollPhone.getText().toString();
                if (isRightPhone(verPhone)) {
                    presenter.registerGetVerification(verPhone);
                    getEnrollVerification.setText("60");
                    getEnrollVerification.setClickable(false);
                    Message message = Message.obtain();
                    message.what = 0;
                    handler.sendMessageDelayed(message, 1000);
                } else {
                    showMessage("手机号有误！");
                }
                break;
            case R.id.activity_login_or_register_enroll_part_btn:
                String rePhone = enrollPhone.getText().toString();
                String reVerification = enrollVerification.getText().toString();
                String rePassword = enrollPassword.getText().toString();
                String reConfirm = enrollConfirm.getText().toString();
                if (isRightRegisterInformation(rePhone, reVerification, rePassword, reConfirm)) {
                    Map<String, String> map = new HashMap<>();
                    map.put("requestCode", "1");
                    map.put("phone", rePhone);
                    map.put("password", rePassword);
                    map.put("confirmPassword", reConfirm);
                    map.put("bizId", verificationBizID);
                    map.put("verification", reVerification);
                    presenter.registerDriver(map);
                }
                break;
        }
    }

    /**
     * 用于判断登录信息是否正确
     *
     * @param phone **手机号**
     * @param password **密码**
     * @return **是否是正确的登录信息**
     */
    private boolean isRightLoginInformation(String phone, String password) {
        if (isRightPhone(phone)) {
            if (isRightPassword(password)) {
                return true;
            } else {
                showMessage("密码不能为空并且长度不能超过20！");
                return false;
            }
        } else {
            showMessage("手机号有误！");
            return false;
        }
    }

    /**
     * 判断注册信息是否正确
     *
     * @param phone
     * @param verification
     * @param password
     * @param confirm
     * @return
     */
    private boolean isRightRegisterInformation(String phone, String verification, String password, String confirm) {
        if (isRightPhone(phone)) {
            if (verification != null && verification.length() == 6) {
                if (isRightPassword(password)) {
                    if (isRightPassword(confirm)) {
                        if (password.equals(confirm)) {
                            if (verificationBizID != null) {
                                return true;
                            } else {
                                showMessage("验证码没有获取或已失效！");
                                return false;
                            }
                        } else {
                            showMessage("密码与确认密码不一致！");
                            return false;
                        }
                    } else {
                        showMessage("确认密码不能为空并且长度不能超过20！");
                        return false;
                    }
                } else {
                    showMessage("密码不能为空并且长度不能超过20！");
                    return false;
                }
            } else {
                showMessage("验证码有误！");
                return false;
            }
        } else {
            showMessage("手机号有误！");
            return false;
        }
    }

    /**
     * 判断手机号是否是个正确的号码
     *
     * @param phone
     * @return
     */
    private boolean isRightPhone(String phone) {
        if (phone != null) {
            String phoneRegEx = "^(13|14|15|17|18|19)[0-9]{9}$";
            return Pattern.compile(phoneRegEx).matcher(phone).matches();
        } else {
            return false;
        }
    }

    /**
     * 判断密码是否是个正确的密码
     *
     * @param paswword
     * @return
     */
    private boolean isRightPassword(String paswword) {
        if (paswword != null) {
            if (paswword.length() > 0 && paswword.length() <= 20) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Toast输出消息
     *
     * @param message 消息内容
     */
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 数据请求成功进行消息处理
     *
     * @param object
     */
    @Override
    public void onSuccess(Object object) {
        if (object instanceof NetDriverData) {
            NetDriverData driverData = (NetDriverData) object;
            if (driverData.getState() == 1001 && driverData.getDriver() != null) {
                presenter.storageDriver(driverData.getDriver());
                driverEntity = driverData.getDriver();
                startHomePagerActivity();
            } else if (driverData.getState() == 1003 && driverData.getVerification_id() != null) {
                verificationBizID = driverData.getVerification_id();
            } else if (driverData.getState() == 1007 && driverData.getDriver() != null) {
                TrainInformationActivity.startTrainInformationActivity(this, driverData.getDriver(), true);
            } else {
                showMessage(driverData.getMessage());
            }
        } else {
            showMessage("数据解析失败！");
        }
    }

    /**
     * 数据请求失败
     *
     * @param message
     */
    @Override
    public void onError(String message) {
        showMessage(message);
    }

    /**
     * 注册的触摸事件
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
                enrollPart.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_out);
                enrollPart.startAnimation(animation);
                return true;
            }
        }
        return false;
    }

    /**
     * 跳转到主界面
     */
    private void startHomePagerActivity() {
        if (driverEntity != null) {
            HomePagerActivity.startHomePager(this, driverEntity, true);
            finish();
        }
    }

    /**
     * 用于判断字符串是否是正确的正整数与0的集合
     *
     * @param num
     * @return
     */
    private boolean isNum(String num) {
        if (num != null) {
            String numRegEx = "^[1-9]\\d*|0$";
            return Pattern.compile(numRegEx).matcher(num).matches();
        } else {
            return false;
        }
    }

    private static class LoginOrRegisterHandler extends Handler {
        private WeakReference<LoginOrRegisterActivity> referenceActivity;

        public LoginOrRegisterHandler(LoginOrRegisterActivity activity) {
            referenceActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String countDown = referenceActivity.get().getEnrollVerification.getText().toString();
                    if (referenceActivity.get().isNum(countDown)) {
                        if (countDown.equals("1")) {
                            referenceActivity.get().getEnrollVerification.setText("重新获取");
                            referenceActivity.get().getEnrollVerification.setClickable(true);
                        } else {
                            Integer num = Integer.valueOf(countDown);
                            referenceActivity.get().getEnrollVerification.setText(String.valueOf(--num));
                            Message message = Message.obtain();
                            message.what = 0;
                            referenceActivity.get().handler.sendMessageDelayed(message, 1000);
                        }
                    }
                    break;
                case 1:
                    break;
            }
        }
    }
}
