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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leezp.learncartrainproject.R;
import com.android.leezp.learncartrainproject.adapter.BookSuccessOrderRecyclerAdapter;
import com.android.leezp.learncartrainproject.entities.BookSuccessOrderAdapterEntity;
import com.android.leezp.learncartrainproject.entities.BookSuccessOrderEntity;
import com.android.leezp.learncartrainproject.entities.DriverEntity;
import com.android.leezp.learncartrainproject.interfaces.NetEvent;
import com.android.leezp.learncartrainproject.interfaces.OnItemLongClickListener;
import com.android.leezp.learncartrainproject.net.data.NetBookSuccessOrderData;
import com.android.leezp.learncartrainproject.net.data.NetRelationBookSuccessOrderData;
import com.android.leezp.learncartrainproject.presenter.BookSuccessOrderPresenter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookSuccessOrderActivity extends AppCompatActivity implements NetEvent, View.OnClickListener, OnItemLongClickListener, View.OnTouchListener {
    private ImageView back;
    private TextView title;
    private RecyclerView recycler;
    private LinearLayout removePart;
    private EditText removePartMessage;
    private TextView removePartConfirm;
    private TextView removePartCancel;
    private BookSuccessOrderPresenter presenter;
    private DriverEntity driverEntity;
    private List<BookSuccessOrderAdapterEntity> data;
    private List<BookSuccessOrderEntity> orderData;
    private Map<String, NetRelationBookSuccessOrderData> relateOrderData;
    private BookSuccessOrderRecyclerAdapter adapter;
    private int dataSize = 0;
    private float startX;
    private float startY;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_success_order);
        back = findViewById(R.id.menu_activity_title_part_back);
        title = findViewById(R.id.menu_activity_title_part_content);
        recycler = findViewById(R.id.activity_book_success_order_recycler);
        removePart = findViewById(R.id.activity_book_success_order_remove_part);
        removePartMessage = findViewById(R.id.activity_book_success_order_remove_part_message);
        removePartConfirm = findViewById(R.id.activity_book_success_order_remove_part_confirm);
        removePartCancel = findViewById(R.id.activity_book_success_order_remove_part_cancel);

        presenter = new BookSuccessOrderPresenter();
        presenter.onCreate();
        presenter.attachEvent(this);

        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new BookSuccessOrderRecyclerAdapter(data);
        adapter.addOnItemLongClickListener(this);
        recycler.setAdapter(adapter);

        Intent intent = getIntent();
        loadData(intent);

        back.setOnClickListener(this);
        removePart.setOnTouchListener(this);
        removePartConfirm.setOnClickListener(this);
        removePartCancel.setOnClickListener(this);
    }

    private void loadData(Intent intent) {
        Serializable serializable = intent.getSerializableExtra("driver");
        if (serializable == null) {
            showMessage("错误打开本页面，请重起本应用！");
            return;
        }
        driverEntity = (DriverEntity) serializable;
        presenter.getBookSuccessOrder(String.valueOf(driverEntity.getId()), driverEntity.getPhone(), driverEntity.getPassword());
        title.setText("预约成功订单");
    }

    public static void startBookSuccessOrderActivity(Activity activity, DriverEntity driverEntity) {
        Intent intent = new Intent(activity, BookSuccessOrderActivity.class);
        intent.putExtra("driver", driverEntity);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onSuccess(Object object) {
        if (object instanceof NetBookSuccessOrderData) {
            NetBookSuccessOrderData netBookSuccessOrderData = (NetBookSuccessOrderData) object;
            if (netBookSuccessOrderData.getState() == 1002 && netBookSuccessOrderData.getComplete_orders() != null) {
                orderData = netBookSuccessOrderData.getComplete_orders();
                dataSize = orderData.size();
                for (BookSuccessOrderEntity entity : orderData) {
                    presenter.getRelationBookSuccessOrder(String.valueOf(entity.getNetId()), String.valueOf(entity.getDriverID()), String.valueOf(entity.getStudentID()));
                }
            } else if (netBookSuccessOrderData.getState() == 1004) {
                showMessage(netBookSuccessOrderData.getMessage());
                removePart.setVisibility(View.GONE);
            } else {
                showMessage(netBookSuccessOrderData.getMessage());
            }
        } else if (object instanceof NetRelationBookSuccessOrderData) {
            --dataSize;
            if (relateOrderData == null) {
                relateOrderData = new HashMap<>();
            }
            NetRelationBookSuccessOrderData netRelationBookSuccessOrderData = (NetRelationBookSuccessOrderData) object;
            relateOrderData.put(netRelationBookSuccessOrderData.getOrder_id(), netRelationBookSuccessOrderData);
            if (dataSize == 0) {
                if (data == null) {
                    data = new ArrayList<>();
                }
                for (BookSuccessOrderEntity entity : orderData) {
                    NetRelationBookSuccessOrderData relationBookSuccessOrderData = relateOrderData.get(String.valueOf(entity.getNetId()));
                    SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    try {
                        Date parse = timeStampFormat.parse(entity.getBeginTime());
                        String dateStr = dateFormat.format(parse);
                        String timeSlotStr = timeFormat.format(parse) + "-" + timeFormat.format(timeStampFormat.parse(entity.getEndTime()));
                        data.add(new BookSuccessOrderAdapterEntity(entity.getNetId(), relationBookSuccessOrderData.getHead_url(), relationBookSuccessOrderData.getName(), "手机号：" + relationBookSuccessOrderData.getPhone(), dateStr + " " + timeSlotStr, entity.getPlace(), "￥" + String.format("%.2f", entity.getPrice())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataChange(data);
            }
        } else {
            showMessage("数据解析异常！");
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
            case R.id.activity_book_success_order_remove_part_cancel:
                removePart.setVisibility(View.GONE);
                break;
            case R.id.activity_book_success_order_remove_part_confirm:
                String removeMessage = removePartMessage.getText().toString();
                if (removeMessage == null) {
                    removeMessage = "";
                }
                presenter.removeBookSuccessOrder(String.valueOf(driverEntity.getId()), driverEntity.getPhone(), driverEntity.getPassword(), String.valueOf(removePart.getTag()), removeMessage);
                break;
        }
    }

    @Override
    public void OnItemLongClick(View view) {
        removePart.setTag(view.getTag());
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fragment_show_part_in);
        removePart.startAnimation(animation);
        removePart.setVisibility(View.VISIBLE);
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
