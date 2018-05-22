package com.android.leezp.learncarproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.leezp.learncarlib.GlideApp;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.android.leezp.learncarproject.entity.CompleteOrderAdapterEntity;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;
import com.android.leezp.learncarproject.utils.listener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leezp on 2018/4/4.
 */

public class CompleteOrderAdapter extends RecyclerView.Adapter {

    class TrailViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView head;
        private TextView name;
        private TextView information;
        private TextView time;
        private TextView address;
        private TextView price;

        public TrailViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            head = ((ImageView) view.findViewById(R.id.fragment_driver_trail_item_head));
            name = ((TextView) view.findViewById(R.id.fragment_driver_trail_item_name));
            information = ((TextView) view.findViewById(R.id.fragment_driver_trail_item_information));
            time = ((TextView) view.findViewById(R.id.fragment_driver_trail_item_time));
            address = ((TextView) view.findViewById(R.id.fragment_driver_trail_item_address));
            price = ((TextView) view.findViewById(R.id.fragment_driver_trail_item_price));
        }
    }

    private List<CompleteOrderAdapterEntity> data;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public CompleteOrderAdapter(List<CompleteOrderAdapterEntity> data) {
        this.data = data;
    }

    public void addOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void addOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public void notifyDataChange(List<CompleteOrderAdapterEntity> data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data.clear();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_driver_trail_item, parent, false);
        TrailViewHolder holder = new TrailViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TrailViewHolder) {
            TrailViewHolder trailViewHolder = (TrailViewHolder) holder;
            CompleteOrderAdapterEntity entity = data.get(position);
            GlideApp.with(trailViewHolder.view)
                    .load(ActivityConstantEntity.SERVER_ADDRESS + entity.getHeadUrl())
                    .placeholder(R.drawable.image_loading_black)
                    .error(R.drawable.personal_driver_trail_default_head)
                    .into(trailViewHolder.head);
            trailViewHolder.name.setText(entity.getName());
            trailViewHolder.information.setText(entity.getInformation());
            trailViewHolder.time.setText(entity.getDateTimeSlot());
            trailViewHolder.address.setText(entity.getPlace());
            trailViewHolder.price.setText(entity.getPrice());
            trailViewHolder.view.setTag(entity);
            trailViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, position);
                }
            });
            trailViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemLongClickListener.onItemLongClick(view);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
