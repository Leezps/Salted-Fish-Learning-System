package com.android.leezp.learncartrainproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leezp.learncartrainproject.R;
import com.android.leezp.learncartrainproject.adapter.MessageCenterRecyclerAdapter;
import com.android.leezp.learncartrainproject.entities.DriverEntity;
import com.android.leezp.learncartrainproject.entities.MessageCenterEntity;
import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.interfaces.OnItemClickListener;
import com.android.leezp.learncartrainproject.interfaces.OnItemLongClickListener;
import com.android.leezp.learncartrainproject.net.data.NetMessageCenterData;
import com.android.leezp.learncartrainproject.presenter.MessageCenterPresenter;

import java.io.Serializable;
import java.util.List;

public class MessageCenterActivity extends AppCompatActivity implements NetEvent, View.OnClickListener, OnItemClickListener, OnItemLongClickListener, View.OnTouchListener {
    private ImageView back;
    private TextView title;
    private RecyclerView recycler;
    private LinearLayout showPart;
    private TextView showPartTitle;
    private TextView showPartDate;
    private TextView showPartContent;
    private LinearLayout removePart;
    private TextView removePartConfirm;
    private TextView removePartCancel;
    private MessageCenterPresenter presenter;
    private List<MessageCenterEntity> data;
    private MessageCenterRecyclerAdapter adapter;
    private DriverEntity driverEntity;
    private float startX;
    private float startY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        back = findViewById(R.id.menu_activity_title_part_back);
        title = findViewById(R.id.menu_activity_title_part_content);
        recycler = findViewById(R.id.activity_message_center_recycler);
        showPart = findViewById(R.id.activity_message_center_show_part);
        showPartTitle = findViewById(R.id.activity_message_center_show_part_title);
        showPartDate = findViewById(R.id.activity_message_center_show_part_date);
        showPartContent = findViewById(R.id.activity_message_center_show_part_content);
        removePart = findViewById(R.id.activity_message_center_remove_part);
        removePartConfirm = findViewById(R.id.activity_message_center_remove_part_confirm);
        removePartCancel = findViewById(R.id.activity_message_center_remove_part_cancel);

        presenter = new MessageCenterPresenter();
        presenter.onCreate();
        presenter.attachEvent(this);

        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new MessageCenterRecyclerAdapter(data);
        adapter.addOnItemClickListener(this);
        adapter.addOnItemLongClickListener(this);
        recycler.setAdapter(adapter);

        Intent intent = getIntent();
        loadData(intent);

        back.setOnClickListener(this);
        showPart.setOnTouchListener(this);
        removePartConfirm.setOnClickListener(this);
        removePartCancel.setOnClickListener(this);
    }

    private void loadData(Intent intent) {
        Serializable serializable = intent.getSerializableExtra("driver");
        if (serializable == null) {
            showMessage("错误打开本页面，请重启本应用！");
            return;
        }
        driverEntity = ((DriverEntity) serializable);
        presenter.getMessageCenter(String.valueOf(driverEntity.getId()), driverEntity.getPhone(), driverEntity.getPassword());
        title.setText("消息中心");
    }

    public static void startMessageCenterActivity(Activity activity, DriverEntity entity) {
        Intent intent = new Intent(activity, MessageCenterActivity.class);
        intent.putExtra("driver", entity);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onSuccess(Object object) {
        if (object instanceof NetMessageCenterData) {
            NetMessageCenterData netMessageCenterData = (NetMessageCenterData) object;
            if (netMessageCenterData.getState() == 1002 && netMessageCenterData.getMessage_center() != null) {
                data = netMessageCenterData.getMessage_center();
                adapter.notifyDataChange(data);
            } else if (netMessageCenterData.getState() == 1006) {
                //TODO 有待考证当中
                MessageCenterEntity entity = (MessageCenterEntity) showPart.getTag();
                entity.setIsOpen(1);
                adapter.notifyDataSetChanged();
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_in);
                showPart.startAnimation(animation);
                showPart.setVisibility(View.VISIBLE);
                showPartTitle.setText(entity.getTitle());
                showPartDate.setText(entity.getDate());
                showPartContent.setText(entity.getContent());
            } else if (netMessageCenterData.getState() == 1004) {
                MessageCenterEntity entity = (MessageCenterEntity) removePart.getTag();
                removePart.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_out);
                removePart.startAnimation(animation);
                data.remove(entity);
                adapter.notifyDataChange(data);
            } else {
                showMessage(netMessageCenterData.getMessage());
            }
        } else {
            showMessage("消息中心数据解析有误！");
        }
    }

    @Override
    public void onError(String message) {
        showMessage(message);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_activity_title_part_back:
                HomePagerActivity.startHomePager(this, driverEntity, false);
                break;
            case R.id.activity_message_center_remove_part_confirm:
                MessageCenterEntity entity = (MessageCenterEntity) removePart.getTag();
                presenter.removeMessageCenter(String.valueOf(driverEntity.getId()), driverEntity.getPhone(), driverEntity.getPassword(), String.valueOf(entity.getNetId()));
                break;
            case R.id.activity_message_center_remove_part_cancel:
                removePart.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_out);
                removePart.startAnimation(animation);
                break;
        }
    }

    @Override
    public void OnItemClick(View view) {
        MessageCenterEntity entity = (MessageCenterEntity) view.getTag();
        if (entity.getIsOpen() == 0) {
            showPart.setTag(entity);
            presenter.updateMessageCenter(String.valueOf(driverEntity.getId()), driverEntity.getPhone(), driverEntity.getPassword(), String.valueOf(entity.getNetId()));
        } else {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_in);
            showPart.startAnimation(animation);
            showPart.setVisibility(View.VISIBLE);
            showPartTitle.setText(entity.getTitle());
            showPartDate.setText(entity.getDate());
            showPartContent.setText(entity.getContent());
        }
    }

    @Override
    public void OnItemLongClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_in);
        removePart.startAnimation(animation);
        removePart.setVisibility(View.VISIBLE);
        removePart.setTag(view.getTag());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            if (Math.abs(y - startY) < 20 && x - startX < 20) {
                if (v.getVisibility() == View.VISIBLE) {
                    v.setVisibility(View.GONE);
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_out);
                    v.startAnimation(animation);
                    return true;
                }
            }
        }
        return false;
    }
}
