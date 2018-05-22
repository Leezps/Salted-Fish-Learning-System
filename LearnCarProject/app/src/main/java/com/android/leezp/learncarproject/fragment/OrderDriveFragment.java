package com.android.leezp.learncarproject.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.activity.HomePagerActivity;
import com.android.leezp.learncarproject.activity.PublishOrderActivity;
import com.android.leezp.learncarproject.adapter.OrderDriverAdapter;
import com.android.leezp.learncarproject.entity.DriverEntity;
import com.android.leezp.learncarproject.interfaces.LoadDataInterface;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Leezp on 2018/4/1.
 */

public class OrderDriveFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadDataInterface, OnItemClickListener {

    private View view;
    private SwipeRefreshLayout refresh;
    private RecyclerView recycler;
    private OrderDriverAdapter adapter;
    private TextView loadContent;
    private ProgressBar loadProgress;

    private int offset = 0;
    private int limit = 5;
    private List<DriverEntity> data = null;
    private OrderDriverHandler handler;
    private boolean isNetworkData = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_driver, container, false);
        refresh = ((SwipeRefreshLayout) view.findViewById(R.id.fragment_order_driver_refresh));
        recycler = ((RecyclerView) view.findViewById(R.id.fragment_order_driver_recycler));

        handler = new OrderDriverHandler(this);

        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new OrderDriverAdapter(data, this);
        adapter.addOnItemClickListener(this);
        recycler.setAdapter(adapter);

        refresh.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onRefresh() {
        isNetworkData = false;
        ((HomePagerActivity) getActivity()).getDriverInformation();
    }

    @Override
    public void requestData(TextView content, ProgressBar load) {
        loadContent = content;
        loadProgress = load;
        if (isNetworkData) {
            ActivityCollector.cachedThreadPool.execute(new OrderDriverRunnable(this));
        }
    }

    /**
     * 网络加载数据后调用此接口更新数据
     */
    public void refreshOrderDriverData() {
        isNetworkData = true;
        offset = 0;
        if (adapter != null) {
            Message message = Message.obtain();
            message.what = 2;
            handler.sendMessage(message);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Object object = view.getTag();
        if (object != null) {
            DriverEntity entity = (DriverEntity) object;
            PublishOrderActivity.startPublishOrderActivity(getActivity(), entity.getNetId(), ((HomePagerActivity) getActivity()).getStudentEntity());
        }
    }

    private static class OrderDriverRunnable implements Runnable {
        private WeakReference<OrderDriveFragment> referenceFragment;

        public OrderDriverRunnable(OrderDriveFragment fragment) {
            this.referenceFragment = new WeakReference<>(fragment);
        }

        @Override
        public void run() {
            loadData();
        }

        private void loadData() {
            int offset = referenceFragment.get().offset;
            int limit = referenceFragment.get().limit;
            int count = DataSupport.count(DriverEntity.class);
            if (offset >= count) {
                Message message = Message.obtain();
                message.what = 0;
                referenceFragment.get().handler.sendMessage(message);
            } else {
                List<DriverEntity> entities = DataSupport.offset(offset)
                        .limit(limit)
                        .find(DriverEntity.class);
                if (entities != null && entities.size() > 0) {
                    referenceFragment.get().data = entities;
                    offset += entities.size();
                    referenceFragment.get().offset = offset;
                    Message message = Message.obtain();
                    message.what = 1;
                    referenceFragment.get().handler.sendMessage(message);
                }
            }
        }
    }

    private static class OrderDriverHandler extends Handler {
        private WeakReference<OrderDriveFragment> referenceFragment;

        public OrderDriverHandler(OrderDriveFragment fragment) {
            referenceFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    referenceFragment.get().loadContent.setText("没有更多订单了");
                    referenceFragment.get().loadProgress.setVisibility(View.GONE);
                    break;
                case 1:
                    referenceFragment.get().adapter.addDataChange(referenceFragment.get().data);
                    break;
                case 2:
                    referenceFragment.get().adapter.notifyDataChange(null);
                    if (referenceFragment.get().refresh.isRefreshing()) {
                        referenceFragment.get().refresh.setRefreshing(false);
                    }
                    break;
            }
        }
    }
}
