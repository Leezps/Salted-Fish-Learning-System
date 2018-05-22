package com.android.leezp.learncartrainproject.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.leezp.learncartrainproject.R;
import com.android.leezp.learncartrainproject.entities.MessageCenterEntity;
import com.android.leezp.learncartrainproject.interfaces.OnItemClickListener;
import com.android.leezp.learncartrainproject.interfaces.OnItemLongClickListener;

import java.util.List;

public class MessageCenterRecyclerAdapter extends RecyclerView.Adapter {
    private List<MessageCenterEntity> data;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public MessageCenterRecyclerAdapter(List<MessageCenterEntity> data) {
        this.data = data;
    }

    public void addOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void addOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public void notifyDataChange(List<MessageCenterEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_message_center_item, parent, false);
        return new MessageCenterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageCenterViewHolder) {
            MessageCenterViewHolder viewHolder = (MessageCenterViewHolder) holder;
            MessageCenterEntity entity = data.get(position);
            viewHolder.title.setText(entity.getTitle());
            viewHolder.content.setText(entity.getContent());
            viewHolder.date.setText(entity.getDate());
            if (entity.getIsOpen() == 0) {
                viewHolder.pointer.setVisibility(View.VISIBLE);
            } else {
                viewHolder.pointer.setVisibility(View.GONE);
            }
            viewHolder.view.setTag(entity);
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.OnItemClick(v);
                }
            });
            viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemLongClickListener.OnItemLongClick(v);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private class MessageCenterViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView title;
        private TextView content;
        private TextView date;
        private ImageView pointer;

        public MessageCenterViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = itemView.findViewById(R.id.activity_message_center_item_title);
            content = itemView.findViewById(R.id.activity_message_center_item_content);
            date = itemView.findViewById(R.id.activity_message_center_item_date);
            pointer = itemView.findViewById(R.id.activity_message_center_item_no_open);
        }
    }
}
