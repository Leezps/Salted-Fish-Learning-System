package com.android.leezp.learncarproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.leezp.learncarlib.GlideApp;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.android.leezp.learncarproject.entity.DriverEntity;
import com.android.leezp.learncarproject.interfaces.LoadDataInterface;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leezp on 2018/4/5.
 */

public class OrderDriverAdapter extends RecyclerView.Adapter {

    private final int LoadView = 30001;
    private final int ItemView = 30002;

    private List<DriverEntity> data;
    private OnItemClickListener itemClickListener;
    private LoadDataInterface requestData;

    public OrderDriverAdapter(List<DriverEntity> data, LoadDataInterface requestData) {
        this.data = data;
        this.requestData = requestData;
    }

    public void notifyDataChange(List<DriverEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addDataChange(List<DriverEntity> data) {
        if (data == null) return;
        if (this.data == null) this.data = new ArrayList<>();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void addOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (data == null || position == data.size()) {
            return LoadView;
        } else {
            return ItemView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LoadView) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_order_driver_item_load, parent, false);
            return new LoadViewHolder(view);
        } else if (viewType == ItemView) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_order_driver_item, parent, false);
            return new DriverViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof LoadViewHolder) {
            LoadViewHolder loadViewHolder = (LoadViewHolder) holder;
            loadViewHolder.content.setText("加载更多");
            loadViewHolder.load.setVisibility(View.VISIBLE);
            requestData.requestData(loadViewHolder.content, loadViewHolder.load);
        } else if (holder instanceof  DriverViewHolder){
            DriverViewHolder viewHolder = (DriverViewHolder) holder;
            DriverEntity entity = data.get(position);
            GlideApp.with(viewHolder.view)
                    .load(ActivityConstantEntity.SERVER_ADDRESS+entity.getHead_url())
                    .placeholder(R.drawable.image_loading_black)
                    .error(R.drawable.order_driver_trainer_head)
                    .into(viewHolder.head);
            viewHolder.name.setText(entity.getName());
            viewHolder.information.setText(entity.getInformation());
            viewHolder.price.setText(String.valueOf(entity.getUnit_price()));
            viewHolder.people.setText(String.valueOf(entity.getTaught_people()));
            viewHolder.evaluate.setText(String.valueOf(entity.getEvaluate()));
            viewHolder.phone.setText(entity.getPhone());
            viewHolder.place.setText(entity.getPlace());
            viewHolder.view.setTag(entity);
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 1 : data.size() + 1;
    }

    class DriverViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView head;
        private TextView name;
        private TextView information;
        private TextView price;
        private TextView people;
        private TextView evaluate;
        private TextView phone;
        private TextView place;
        private TextView distance;

        public DriverViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            head = ((ImageView) view.findViewById(R.id.fragment_order_driver_item_head));
            name = ((TextView) view.findViewById(R.id.fragment_order_driver_item_name));
            information = ((TextView) view.findViewById(R.id.fragment_order_driver_item_information));
            price = ((TextView) view.findViewById(R.id.fragment_order_driver_item_price));
            people = ((TextView) view.findViewById(R.id.fragment_order_driver_item_people));
            evaluate = ((TextView) view.findViewById(R.id.fragment_order_driver_item_evaluate));
            phone = ((TextView) view.findViewById(R.id.fragment_order_driver_item_phone));
            place = ((TextView) view.findViewById(R.id.fragment_order_driver_item_place));
            distance = ((TextView) view.findViewById(R.id.fragment_order_driver_item_distance));
        }

    }

    class LoadViewHolder extends RecyclerView.ViewHolder {
        private TextView content;
        private ProgressBar load;

        public LoadViewHolder(View itemView) {
            super(itemView);
            content = ((TextView) itemView.findViewById(R.id.fragment_order_driver_item_load_content));
            load = ((ProgressBar) itemView.findViewById(R.id.fragment_order_driver_item_load_progress));
        }

    }
}
