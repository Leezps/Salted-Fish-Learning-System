package com.android.leezp.learncarproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leezp.learncarlib.activity.BaseActivity;
import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.db.RegisterDB;
import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.android.leezp.learncarproject.presenter.RegisterPresenter;
import com.android.leezp.learncarproject.utils.TypeTransform;
import com.android.leezp.learncarproject.utils.event.NetEvent;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leezp on 2018/4/2.
 */

public class UserRegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText phone;
    private EditText verification;
    private TextView codeBtn;
    private EditText password;
    private EditText confirm;
    private TextView register;

    private MyHandler handler;
    private String bizId = null;
    private RegisterPresenter presenter = new RegisterPresenter();
    private NetEvent event = new NetEvent() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof RegisterDB) {
                RegisterDB registerDB = (RegisterDB) object;
                switch (registerDB.getState()) {
                    case 1003:
                        bizId = registerDB.getVerificationId();
                        break;
                    case 1007:
                        SharedPreferences preferences = getSharedPreferences(ActivityConstantEntity.personalInformation_sharePreferences_name, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("personalInformation", new Gson().toJson(registerDB.getStudent()));
                        editor.apply();
                        PersonalInformationActivity.startPersonalInformationActivity(UserRegisterActivity.this, "上传个人信息", ActivityConstantEntity.registerPersonalInformation);
                        break;
                    default:
                        showMessage(registerDB.getMessage());
                        if (TypeTransform.canStrToInt(codeBtn.getText().toString())) {
                            handler.removeMessages(Integer.valueOf(codeBtn.getText().toString()) - 1);
                        }
                        codeBtn.setText("重新获取");
                        codeBtn.setClickable(true);
                        register.setClickable(true);
                }
            }
        }

        @Override
        public void onError(String message) {
            showMessage(message);
            if (TypeTransform.canStrToInt(codeBtn.getText().toString())) {
                handler.removeMessages(Integer.valueOf(codeBtn.getText().toString()) - 1);
            }
            codeBtn.setText("重新获取");
            codeBtn.setClickable(true);
            register.setClickable(true);
        }
    };

    @Override
    protected void initVariables() {
        ActivityCollector.addActivity(this);
        handler = new MyHandler(this);
        presenter.onCreate();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_register);
        phone = ((EditText) findViewById(R.id.activity_user_register_phone));
        verification = ((EditText) findViewById(R.id.activity_user_register_verification));
        codeBtn = ((TextView) findViewById(R.id.activity_user_register_code_btn));
        password = ((EditText) findViewById(R.id.activity_user_register_password));
        confirm = ((EditText) findViewById(R.id.activity_user_register_confirm));
        register = ((TextView) findViewById(R.id.activity_user_register_btn));

        codeBtn.setOnClickListener(this);
        register.setOnClickListener(this);
        presenter.attachEvent(event);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public static void startUserRegisterActivity(Activity activity) {
        Intent intent = new Intent(activity, UserRegisterActivity.class);
        activity.startActivity(intent);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        String phone_str = null;
        switch (view.getId()) {
            case R.id.activity_user_register_code_btn:
                phone_str = phone.getText().toString();
                if (phone_str != null && phone_str.length() == 11) {
                    codeBtn.setClickable(false);
                    handler.sendEmptyMessage(60);
                    presenter.requestVerification("0", phone_str);
                } else {
                    showMessage("手机号不正确！");
                }
                break;
            case R.id.activity_user_register_btn:
                phone_str = phone.getText().toString();
                String verification_str = verification.getText().toString();
                String password_str = password.getText().toString();
                String confirm_str = confirm.getText().toString();
                if (bizId == null) {
                    showMessage("请重新获取验证码！");
                } else if (password_str == null
                        || verification_str == null || password_str == null
                        || confirm_str == null || phone_str.length() != 11
                        || password_str.length() > 20 || !password_str.equals(confirm_str)) {
                    showMessage("信息有误，请仔细审核信息！");
                } else {
                    register.setClickable(false);
                    Map<String, String> params = new HashMap<>();
                    params.put("requestCode", "1");
                    params.put("phone", phone_str);
                    params.put("password", password_str);
                    params.put("confirmPassword", confirm_str);
                    params.put("bizId", bizId);
                    params.put("verification", verification_str);
                    presenter.registerStudent(params);
                }
                break;
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<UserRegisterActivity> activities;

        public MyHandler(UserRegisterActivity activity) {
            activities = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what != 0) {
                activities.get().codeBtn.setText(String.valueOf(what));
                sendEmptyMessageDelayed(what - 1, 1000);
            } else {
                activities.get().codeBtn.setText("重新获取");
                activities.get().codeBtn.setClickable(true);
            }
        }
    }
}
