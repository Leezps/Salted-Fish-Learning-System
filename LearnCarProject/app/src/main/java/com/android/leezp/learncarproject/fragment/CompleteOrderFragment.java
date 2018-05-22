package com.android.leezp.learncarproject.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.activity.PersonalMenuActivity;
import com.android.leezp.learncarproject.adapter.CompleteOrderAdapter;
import com.android.leezp.learncarproject.db.RelationCompleteOrderDB;
import com.android.leezp.learncarproject.entity.CompleteOrderAdapterEntity;
import com.android.leezp.learncarproject.entity.CompleteOrderEntity;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;
import com.android.leezp.learncarproject.utils.listener.OnItemLongClickListener;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Leezp on 2018/4/2.
 */

public class CompleteOrderFragment extends Fragment implements OnItemClickListener, View.OnClickListener, View.OnTouchListener, OnItemLongClickListener {

    private View view;
    private RecyclerView trailRecycler;
    private CompleteOrderAdapter adapter;
    private List<CompleteOrderAdapterEntity> data = null;
    private int noPerfectDataSize = 0;
    private CompleteOrderHandler handler;
    private LinearLayout removePart;
    private EditText removePartMessage;
    private TextView removePartConfirm;
    private TextView removePartCancel;
    private float startX;
    private float startY;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_driver_trail, container, false);
        trailRecycler = ((RecyclerView) view.findViewById(R.id.fragment_driver_trail_recycler));
        removePart = ((LinearLayout) view.findViewById(R.id.fragment_driver_trail_remove_part));
        removePartMessage = ((EditText) view.findViewById(R.id.fragment_driver_trail_remove_part_message));
        removePartConfirm = ((TextView) view.findViewById(R.id.fragment_driver_trail_remove_part_confirm));
        removePartCancel = ((TextView) view.findViewById(R.id.fragment_driver_trail_remove_part_cancel));

        trailRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        trailRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new CompleteOrderAdapter(data);
        adapter.addOnItemClickListener(this);
        adapter.addOnItemLongClickListener(this);
        trailRecycler.setAdapter(adapter);

        removePart.setOnTouchListener(this);
        removePartConfirm.setOnClickListener(this);
        removePartCancel.setOnClickListener(this);

        handler = new CompleteOrderHandler(this);
        ActivityCollector.cachedThreadPool.execute(new CompleteOrderRunnable(this));
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "功能还未开发", Toast.LENGTH_SHORT).show();
    }

    public void perfectData(RelationCompleteOrderDB relationOrderDB) {
        ActivityCollector.cachedThreadPool.execute(new CompleteOrderRunnable(this, 1, relationOrderDB));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_driver_trail_remove_part_cancel:
                removePart.setVisibility(View.GONE);
                break;
            case R.id.fragment_driver_trail_remove_part_confirm:
                CompleteOrderAdapterEntity entity = (CompleteOrderAdapterEntity) removePart.getTag();
                String removeMsg = removePartMessage.getText().toString();
                ActivityCollector.cachedThreadPool.execute(new CompleteOrderRunnable(this, 2, entity.getNetId(), removeMsg));
                removePart.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            startX = motionEvent.getX();
            startY = motionEvent.getY();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (Math.abs(startY - y) < 20 && x - startX < 20) {
                removePart.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fragment_show_part_out);
                removePart.startAnimation(animation);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemLongClick(View view) {
        removePart.setTag(view.getTag());
        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fragment_show_part_in);
        removePart.startAnimation(animation);
        removePart.setVisibility(View.VISIBLE);
    }

    private static class CompleteOrderRunnable implements Runnable {
        private WeakReference<CompleteOrderFragment> referenceFragment;
        private WeakReference<RelationCompleteOrderDB> referenceRelateDB = null;
        private int operatorWay = 0;
        private int netId = 0;
        private String removeMessage = null;

        public CompleteOrderRunnable(CompleteOrderFragment fragment) {
            referenceFragment = new WeakReference<>(fragment);
        }

        public CompleteOrderRunnable(CompleteOrderFragment fragment, int operatorWay, RelationCompleteOrderDB relateDB) {
            referenceFragment = new WeakReference<>(fragment);
            referenceRelateDB = new WeakReference<>(relateDB);
            this.operatorWay = operatorWay;
        }

        public CompleteOrderRunnable(CompleteOrderFragment fragment, int operatorWay, int netId, String removeMessage) {
            referenceFragment = new WeakReference<>(fragment);
            this.operatorWay = operatorWay;
            this.netId = netId;
            this.removeMessage = removeMessage;
        }

        @Override
        public void run() {
            if (operatorWay == 0) {
                loadCompleteOrder();
            } else if (operatorWay == 1) {
                perfectCompleteOrderAdapter();
            } else if (operatorWay == 2) {
                removeCompleteOrder();
            }
        }

        private void removeCompleteOrder() {
            List<CompleteOrderEntity> entities = DataSupport.select("netId").where("netId=?", String.valueOf(this.netId)).find(CompleteOrderEntity.class);
            if (entities.size() > 0) {
                ((PersonalMenuActivity) referenceFragment.get().getActivity()).removeCompleteOrder(String.valueOf(this.netId), removeMessage);
            }
        }

        private void perfectCompleteOrderAdapter() {
            int size = referenceFragment.get().data.size();
            for (int i = 0; i < size; ++i) {
                CompleteOrderAdapterEntity entity = referenceFragment.get().data.get(i);
                if (referenceRelateDB.get().getOrder_id().equals(String.valueOf(entity.getNetId()))) {
                    synchronized (CompleteOrderRunnable.class) {
                        --referenceFragment.get().noPerfectDataSize;
                    }
                    entity.setHeadUrl(referenceRelateDB.get().getHead_url());
                    entity.setName(referenceRelateDB.get().getName());
                    entity.setInformation("手机号：" + referenceRelateDB.get().getPhone());
                }
            }
            if (referenceFragment.get().noPerfectDataSize == 0) {
                Message message = Message.obtain();
                message.what = 0;
                referenceFragment.get().handler.sendMessage(message);
            }
        }

        private void loadCompleteOrder() {
            List<CompleteOrderEntity> entities = DataSupport.findAll(CompleteOrderEntity.class);
            if (entities != null && entities.size() > 0) {
                referenceFragment.get().noPerfectDataSize = entities.size();
                List<CompleteOrderAdapterEntity> adapterData = new ArrayList<>();
                for (CompleteOrderEntity entity : entities) {
                    ((PersonalMenuActivity) referenceFragment.get().getActivity()).getRelationCompleteOrder(String.valueOf(entity.getNetId()), String.valueOf(entity.getDriverID()), String.valueOf(entity.getStudentID()));
                    SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    try {
                        Date parse = timeStampFormat.parse(entity.getBeginTime());
                        String date = dateFormat.format(parse);
                        String timeSlot = timeFormat.format(parse) + "-" + timeFormat.format(timeStampFormat.parse(entity.getEndTime()));
                        adapterData.add(new CompleteOrderAdapterEntity(entity.getNetId(), date + " " + timeSlot, entity.getPlace(), String.format("%.2f", entity.getPrice())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                referenceFragment.get().data = adapterData;
            }
        }
    }

    private static class CompleteOrderHandler extends Handler {
        WeakReference<CompleteOrderFragment> referenceFragment;

        public CompleteOrderHandler(CompleteOrderFragment fragment) {
            referenceFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    referenceFragment.get().adapter.notifyDataChange(referenceFragment.get().data);
                    break;
            }
        }
    }
}
