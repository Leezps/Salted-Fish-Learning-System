package com.android.leezp.learncartrainproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.android.leezp.learncartrainproject.adapter.HelpRecyclerAdapter;
import com.android.leezp.learncartrainproject.entities.DriverEntity;
import com.android.leezp.learncartrainproject.entities.HelpEntity;
import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.interfaces.OnItemClickListener;
import com.android.leezp.learncartrainproject.net.data.NetHelpData;
import com.android.leezp.learncartrainproject.presenter.HelpPresenter;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener, NetEvent, OnItemClickListener, View.OnTouchListener {
    private ImageView back;
    private TextView title;
    private RecyclerView recycler;
    private TextView opinion;
    private LinearLayout showPart;
    private TextView showPartTitle;
    private TextView showPartContent;
    private DriverEntity driverEntity;
    private HelpPresenter presenter;
    private List<HelpEntity> data;
    private HelpRecyclerAdapter adapter;
    private HelpHandler handler;
    private float startX;
    private float startY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        back = findViewById(R.id.menu_activity_title_part_back);
        title = findViewById(R.id.menu_activity_title_part_content);
        recycler = findViewById(R.id.activity_help_recycler);
        opinion = findViewById(R.id.activity_help_opinion);
        showPart = findViewById(R.id.activity_help_show_part);
        showPartTitle = findViewById(R.id.activity_help_show_part_title);
        showPartContent = findViewById(R.id.activity_help_show_part_content);
        presenter = new HelpPresenter();
        presenter.onCreate();
        presenter.attachEvent(this);

        handler = new HelpHandler(this);

        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new HelpRecyclerAdapter(data);
        adapter.addOnItemClickListener(this);
        recycler.setAdapter(adapter);

        Intent intent = getIntent();
        loadData(intent);

        back.setOnClickListener(this);
        opinion.setOnClickListener(this);
        showPart.setOnTouchListener(this);
    }

    private void loadData(Intent intent) {
        Serializable serializable = intent.getSerializableExtra("driver");
        if (serializable == null) {
            showMessage("错误打开本页面，请重启本应用！");
            return;
        }
        driverEntity = ((DriverEntity) serializable);
        title.setText("应用帮助");
        presenter.showAllHelpList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_activity_title_part_back:
                HomePagerActivity.startHomePager(this, driverEntity, false);
                break;
            case R.id.activity_help_opinion:
                Toast.makeText(this, "该功能在下个版本开发", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static void startHelpActivity(Activity activity, DriverEntity entity) {
        Intent intent = new Intent(activity, HelpActivity.class);
        intent.putExtra("driver", entity);
        activity.startActivity(intent);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public void refreshHelpData(List<HelpEntity> data) {
        this.data = data;
        Message message = Message.obtain();
        message.what = 0;
        handler.sendMessage(message);
    }

    @Override
    public void onSuccess(Object object) {
        if (object instanceof NetHelpData) {
            NetHelpData helpData = (NetHelpData) object;
            if (helpData.getState() == 1001) {
                presenter.storageApplicationHelp(helpData);
            }
        } else {
            showMessage("帮助数据解析异常！");
        }
    }

    @Override
    public void onError(String message) {
        showMessage(message);
    }

    @Override
    public void OnItemClick(View view) {
        HelpEntity entity = (HelpEntity) view.getTag();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_in);
        showPart.startAnimation(animation);
        showPart.setVisibility(View.VISIBLE);
        showPartTitle.setText(entity.getTitle());
        showPartContent.setText(entity.getContent());
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
                showPart.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_out);
                showPart.startAnimation(animation);
                return true;
            }
        }
        return false;
    }

    private static class HelpHandler extends Handler {
        private WeakReference<HelpActivity> referenceActivity;

        public HelpHandler(HelpActivity activity) {
            referenceActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    referenceActivity.get().adapter.notifyDataChange(referenceActivity.get().data);
                    break;
            }
        }
    }
}
