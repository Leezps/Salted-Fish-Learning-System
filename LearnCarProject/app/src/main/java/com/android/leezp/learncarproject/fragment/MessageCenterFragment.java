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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.activity.PersonalMenuActivity;
import com.android.leezp.learncarproject.adapter.MessageCenterAdapter;
import com.android.leezp.learncarproject.entity.MessageCenterEntity;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;
import com.android.leezp.learncarproject.utils.listener.OnItemLongClickListener;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Leezp on 2018/4/2.
 */

public class MessageCenterFragment extends Fragment implements OnItemClickListener, View.OnTouchListener, OnItemLongClickListener, View.OnClickListener {

    private View view;
    private RecyclerView messageRecycler;
    private MessageCenterAdapter adapter;
    private LinearLayout messagePart;
    private TextView messageTitle;
    private TextView messageContent;
    private TextView messageDate;
    private LinearLayout removePart;
    private TextView removeCancel;
    private TextView removeConfirm;

    private List<MessageCenterEntity> data = null;
    private float startX;
    private float startY;
    private MessageHandler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message_center, container, false);
        messageRecycler = ((RecyclerView) view.findViewById(R.id.fragment_message_center_recycler));
        messagePart = ((LinearLayout) view.findViewById(R.id.fragment_message_center_part));
        messageTitle = ((TextView) view.findViewById(R.id.fragment_message_center_part_title));
        messageDate = ((TextView) view.findViewById(R.id.fragment_message_center_part_date));
        messageContent = ((TextView) view.findViewById(R.id.fragment_message_center_part_content));
        removePart = ((LinearLayout) view.findViewById(R.id.fragment_message_center_remove_part));
        removeCancel = ((TextView) view.findViewById(R.id.fragment_message_center_remove_part_cancel));
        removeConfirm = ((TextView) view.findViewById(R.id.fragment_message_center_remove_part_confirm));

        handler = new MessageHandler(this);

        messageRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        messageRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new MessageCenterAdapter(data);
        adapter.addOnItemClickListener(this);
        adapter.addOnItemLongClickListener(this);
        messageRecycler.setAdapter(adapter);

        messagePart.setOnTouchListener(this);
        removeCancel.setOnClickListener(this);
        removeConfirm.setOnClickListener(this);
        loadData();
        return view;
    }

    private void loadData() {
        MessageCenterRunnable runable = new MessageCenterRunnable(this);
        runable.init(0, 0);
        ActivityCollector.cachedThreadPool.execute(runable);
    }

    @Override
    public void onItemClick(View view, int position) {
        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fragment_show_part_in);
        messagePart.startAnimation(animation);
        messagePart.setVisibility(View.VISIBLE);
        MessageCenterEntity entity = data.get(position);
        Object object = view.getTag();
        if (entity == null) {
            if (object != null) {
                MessageCenterRunnable runable = new MessageCenterRunnable(this);
                runable.init(1, Integer.valueOf(String.valueOf(object)));
                ActivityCollector.cachedThreadPool.execute(runable);
            }
        } else {
            messageTitle.setText(entity.getTitle());
            messageDate.setText(entity.getDate().toString());
            messageContent.setText(entity.getContent());
        }
        ImageView noOpen = (ImageView) view.findViewById(R.id.fragment_message_center_item_no_open);
        if (noOpen.getVisibility() == View.VISIBLE) {
            MessageCenterRunnable runable = new MessageCenterRunnable(this);
            runable.init(3, Integer.valueOf(String.valueOf(object)));
            ActivityCollector.cachedThreadPool.execute(runable);
            adapter.changeDataOpenState(Integer.valueOf(String.valueOf(object)));
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
                messagePart.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fragment_show_part_out);
                messagePart.startAnimation(animation);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemLongClick(View view) {
        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fragment_show_part_in);
        removePart.startAnimation(animation);
        removePart.setVisibility(View.VISIBLE);
        Object object = view.getTag();
        if (object != null) {
            removePart.setTag(object);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_message_center_remove_part_confirm:
                Object object = removePart.getTag();
                //先删除本地数据库的数据，在请求删除网络的数据
                if (object != null) {
                    MessageCenterRunnable runable = new MessageCenterRunnable(this);
                    runable.init(2, Integer.valueOf(String.valueOf(object)));
                    ActivityCollector.cachedThreadPool.execute(runable);
                    MessageCenterRunnable messageCenterRunable = new MessageCenterRunnable(this);
                    messageCenterRunable.init(0, 0);
                    ActivityCollector.cachedThreadPool.execute(messageCenterRunable);
                }
            case R.id.fragment_message_center_remove_part_cancel:
                removePart.setVisibility(View.GONE);
                Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.fragment_show_part_out);
                removePart.startAnimation(animation);
                break;
        }
    }

    public void refreshRecyclerView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataChange(data);
            }
        });
    }

    private static class MessageCenterRunnable implements Runnable {
        private WeakReference<MessageCenterFragment> reference = null;
        private int operateWay = 0;
        private int netId = 0;

        public MessageCenterRunnable(MessageCenterFragment fragment) {
            this.reference = new WeakReference<>(fragment);
        }

        /**
         * 初始数据
         *
         * @param operateWay 数据处理方式
         * @param netId      查询数据需要的条件
         */
        public void init(int operateWay, int netId) {
            this.operateWay = operateWay;
            this.netId = netId;
        }

        @Override
        public void run() {
            if (operateWay == 0) {
                List<MessageCenterEntity> entities = DataSupport.findAll(MessageCenterEntity.class);
                reference.get().data = entities;
                reference.get().refreshRecyclerView();
            } else if (operateWay == 1) {
                List<MessageCenterEntity> entities = DataSupport.select("title", "date", "content")
                        .where("netId=?", String.valueOf(netId))
                        .find(MessageCenterEntity.class);
                if (entities.size() > 0) {
                    MessageCenterEntity entity = entities.get(0);
                    Message message = reference.get().handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", entity.getTitle());
                    bundle.putString("date", entity.getDate().toString());
                    bundle.putString("content", entity.getContent());
                    message.setData(bundle);
                    reference.get().handler.sendMessage(message);
                }
            } else if (operateWay == 2) {
                int rowNum = DataSupport.deleteAll(MessageCenterEntity.class, "netId=?", String.valueOf(netId));
                if (rowNum > 0) {
                    ((PersonalMenuActivity) reference.get().getActivity()).removeOrUpdateMessage(netId, 1);
                }
            } else if (operateWay == 3) {
                List<MessageCenterEntity> entities = DataSupport.where("netId=? and isOpen=0", String.valueOf(netId))
                        .find(MessageCenterEntity.class);
                if (entities != null && entities.size() > 0) {
                    MessageCenterEntity entity = entities.get(0);
                    entity.setIsOpen(1);
                    entity.save();
                    ((PersonalMenuActivity) reference.get().getActivity()).removeOrUpdateMessage(netId, 2);
                }
            }
        }
    }

    private static class MessageHandler extends Handler {
        private WeakReference<MessageCenterFragment> reference = null;

        public MessageHandler(MessageCenterFragment fragment) {
            reference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle != null) {
                String title = bundle.getString("title", null);
                String date = bundle.getString("date", null);
                String content = bundle.getString("content", null);
                if (title != null) reference.get().messageTitle.setText(title);
                if (date != null) reference.get().messageDate.setText(date);
                if (content != null) reference.get().messageContent.setText(content);
            }
        }
    }
}
