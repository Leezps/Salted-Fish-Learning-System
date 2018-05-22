package com.android.leezp.learncarproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leezp.learncarlib.activity.BaseFragmentActivity;
import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.db.CompleteOrderDB;
import com.android.leezp.learncarproject.db.MessageCenterDB;
import com.android.leezp.learncarproject.db.PaymentWayDB;
import com.android.leezp.learncarproject.db.RelationCompleteOrderDB;
import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.android.leezp.learncarproject.entity.PaymentWayEntity;
import com.android.leezp.learncarproject.entity.StudentEntity;
import com.android.leezp.learncarproject.fragment.CompleteOrderFragment;
import com.android.leezp.learncarproject.fragment.HelpFragment;
import com.android.leezp.learncarproject.fragment.MessageCenterFragment;
import com.android.leezp.learncarproject.fragment.PaymentWayFragment;
import com.android.leezp.learncarproject.presenter.PersonalMenuPresenter;
import com.android.leezp.learncarproject.utils.event.NetEvent;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leezp on 2018/4/2.
 */

public class PersonalMenuActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView title;
    private ImageView backBtn;
    private FrameLayout container;
    private PersonalMenuPresenter presenter;
    private StudentEntity studentEntity;
    private SharedPreferences preferences;

    private NetEvent event = new NetEvent() {
        @Override
        public void onSuccess(Object object) {
            if (object instanceof MessageCenterDB) {
                MessageCenterDB messageCenterDB = (MessageCenterDB) object;
                showMessage(messageCenterDB.getMessage());
            } else if (object instanceof StudentEntity) {
                studentEntity = ((StudentEntity) object);
            } else if (object instanceof PaymentWayDB) {
                PaymentWayDB paymentWayDB = (PaymentWayDB) object;
                switch (paymentWayDB.getState()) {
                    case 1004:
                        presenter.storageDataBase(paymentWayDB);
                        break;
                    default:
                        showMessage(paymentWayDB.getMessage());
                }
            } else if (object instanceof RelationCompleteOrderDB) {
                RelationCompleteOrderDB relationOrderDB = (RelationCompleteOrderDB) object;
                switch (relationOrderDB.getState()) {
                    case 1002:
                        completeOrderFragment.perfectData(relationOrderDB);
                        break;
                    default:
                        showMessage(relationOrderDB.getMessage());
                }
            } else if(object instanceof CompleteOrderDB) {
                CompleteOrderDB completeOrderDB = (CompleteOrderDB) object;
                showMessage(completeOrderDB.getMessage());
            } else {
                showMessage("数据解析异常！");
            }
        }

        @Override
        public void onError(String message) {
            showMessage(message);
        }
    };

    private PaymentWayFragment paymentWayFragment;
    private CompleteOrderFragment completeOrderFragment;
    private MessageCenterFragment messageCenterFragment;
    private HelpFragment helpFragment;

    @Override
    protected void initVariables() {
        ActivityCollector.addActivity(this);

        presenter = new PersonalMenuPresenter(this);
        presenter.onCreate();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_menu);
        title = ((TextView) findViewById(R.id.activity_personal_menu_title));
        backBtn = ((ImageView) findViewById(R.id.activity_personal_menu_back));
        container = ((FrameLayout) findViewById(R.id.activity_personal_menu_container));

        backBtn.setOnClickListener(this);

        presenter.attachEvent(event);
        preferences = getSharedPreferences(ActivityConstantEntity.personalInformation_sharePreferences_name, MODE_PRIVATE);
        presenter.setPreferences(preferences);
        presenter.getUserInformation();
    }

    @Override
    protected void loadData() {
        Intent intent = getIntent();
        String titleStr = intent.getStringExtra("Title");
        title.setText(titleStr);
        int fragmentId = intent.getIntExtra("FragmentId", 0);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (fragmentId) {
            case ActivityConstantEntity.personalMenuPaymentWay:
                paymentWayFragment = new PaymentWayFragment();
                transaction.add(R.id.activity_personal_menu_container, paymentWayFragment);
                break;
            case ActivityConstantEntity.personalMenuDriverTrail:
                completeOrderFragment = new CompleteOrderFragment();
                transaction.add(R.id.activity_personal_menu_container, completeOrderFragment);
                break;
            case ActivityConstantEntity.personalMenuMessageCenter:
                messageCenterFragment = new MessageCenterFragment();
                transaction.add(R.id.activity_personal_menu_container, messageCenterFragment);
                break;
            case ActivityConstantEntity.personalMenuHelp:
                helpFragment = new HelpFragment();
                transaction.add(R.id.activity_personal_menu_container, helpFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    /**
     * 启动PersonMenuActivity的方法
     *
     * @param activity 上一个activity
     * @param title    启动之后标题的显示信息
     * @param id       根据id加入对应的Fragment
     */
    public static void startPersonalMenuActivity(Activity activity, String title, int id) {
        Intent intent = new Intent(activity, PersonalMenuActivity.class);
        intent.putExtra("Title", title);
        intent.putExtra("FragmentId", id);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_personal_menu_back:
                ActivityCollector.removeActivity(this);
                presenter.onDestroy();
                finish();
                break;
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void removeOrUpdateMessage(int netId, int code) {
        if (studentEntity != null) {
            Map<String, String> params = new HashMap<>();
            params.put("code", String.valueOf(code));
            params.put("role", "0");
            params.put("id", String.valueOf(studentEntity.getStudentId()));
            params.put("phone", studentEntity.getPhone());
            params.put("password", studentEntity.getPassword());
            params.put("message_id", String.valueOf(netId));
            presenter.removeOrUpdateMessage(params);
        }
    }

    public  void removePaymentWay(int netId) {
        if (studentEntity != null) {
            Map<String, String> params = new HashMap<>();
            params.put("code", "2");
            params.put("id", String.valueOf(studentEntity.getStudentId()));
            params.put("phone", studentEntity.getPhone());
            params.put("password", studentEntity.getPassword());
            params.put("payment_id", String.valueOf(netId));
            presenter.removeOrAddPaymentWay(params);
        }
    }

    public void addPaymentWay(PaymentWayEntity paymentEntity) {
        if (studentEntity != null) {
            Map<String,String> params = new HashMap<>();
            params.put("code", "1");
            params.put("id", String.valueOf(studentEntity.getStudentId()));
            params.put("phone", studentEntity.getPhone());
            params.put("password", studentEntity.getPassword());
            Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getName().contains("showRemove");
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            }).create();
            params.put("payment_way", gson.toJson(paymentEntity));
            presenter.removeOrAddPaymentWay(params);
        }
    }

    public void refreshPaymentWayFragment() {
        paymentWayFragment.refreshPaymentWayRecycler();
    }

    public void getRelationCompleteOrder(String orderID, String driverID, String studentID) {
        presenter.getRelationCompleteOrder("0", orderID, driverID, studentID);
    }

    public void removeCompleteOrder(String order_id, String remove_message) {
        if (studentEntity != null) {
            Map<String, String> params = new HashMap<>();
            params.put("code", "1");
            params.put("role", "0");
            params.put("id", String.valueOf(studentEntity.getStudentId()));
            params.put("phone", studentEntity.getPhone());
            params.put("password", studentEntity.getPassword());
            params.put("order_id", order_id);
            params.put("remove_message", remove_message);
            presenter.requestRemoveCompleteOrder(params);
        }
    }
}
