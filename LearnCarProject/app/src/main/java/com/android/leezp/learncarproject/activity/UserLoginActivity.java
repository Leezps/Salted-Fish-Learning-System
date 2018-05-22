package com.android.leezp.learncarproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leezp.learncarlib.activity.BaseActivity;
import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.db.LoginDB;
import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.android.leezp.learncarproject.presenter.LoginPresenter;
import com.android.leezp.learncarproject.utils.event.NetEvent;
import com.google.gson.Gson;

/**
 * Created by Leezp on 2018/4/1.
 */

public class UserLoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView gotoRegisterBtn;
    private EditText phone;
    private TextView password;
    private TextView gotoForgetBtn;
    private TextView loginBtn;

    private LoginPresenter presenter = new LoginPresenter();
    private NetEvent event = new NetEvent() {
        @Override
        public void onSuccess(Object data) {
            if (data instanceof LoginDB) {
                LoginDB loginDB = ((LoginDB) data);
                if (loginDB.getState() == 1001) {
                    SharedPreferences preferences = getSharedPreferences(ActivityConstantEntity.personalInformation_sharePreferences_name, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("personalInformation", new Gson().toJson(loginDB.getStudent()));
                    editor.apply();
                   HomePagerActivity.startHomePagerActivity(UserLoginActivity.this);
                } else {
                    Toast.makeText(UserLoginActivity.this, loginDB.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(UserLoginActivity.this, "数据解析有误！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(UserLoginActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void initVariables() {
        ActivityCollector.addActivity(this);
        presenter.onCreate();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_login);
        gotoRegisterBtn = ((TextView) findViewById(R.id.activity_user_login_goto_register));
        phone = ((EditText) findViewById(R.id.activity_user_login_phone));
        password = ((TextView) findViewById(R.id.activity_user_login_password));
        gotoForgetBtn = ((TextView) findViewById(R.id.activity_user_login_goto_forget));
        loginBtn = ((TextView) findViewById(R.id.activity_user_login_btn));

        gotoRegisterBtn.setOnClickListener(this);
        gotoForgetBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
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

    public static void startUserLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, UserLoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_user_login_goto_register:
                UserRegisterActivity.startUserRegisterActivity(this);
                break;
            case R.id.activity_user_login_btn:
                presenter.requestStudentLogin(phone.getText().toString(), password.getText().toString());
                break;
            case R.id.activity_user_login_goto_forget:
                Toast.makeText(this, "下个版本开放此功能！", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
