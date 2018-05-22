package com.android.leezp.learncarproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.entity.MessageCenterEntity;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;
import com.android.leezp.learncarproject.utils.listener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leezp on 2018/4/4.
 */

public class MessageCenterAdapter extends RecyclerView.Adapter {
    class MessageViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView title;
        private TextView content;
        private TextView date;
        private ImageView noOpen;

        public MessageViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = ((TextView) view.findViewById(R.id.fragment_message_center_item_title));
            content = ((TextView) view.findViewById(R.id.fragment_message_center_item_content));
            date = ((TextView) view.findViewById(R.id.fragment_message_center_item_date));
            noOpen = ((ImageView) view.findViewById(R.id.fragment_message_center_item_no_open));
        }
    }

    private List<MessageCenterEntity> data;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public MessageCenterAdapter(List<MessageCenterEntity> data) {
        this.data = data;
    }

    public void notifyDataChange(List<MessageCenterEntity> data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data.clear();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void changeDataOpenState(int netId) {
        if (data != null) {
            for (int i = 0; i < data.size(); ++i) {
                if (data.get(i).getNetId() == netId) {
                    data.get(i).setIsOpen(1);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void addOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void addOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_message_center_item, parent, false);
        MessageViewHolder holder = new MessageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MessageViewHolder) {
            MessageViewHolder viewHolder = (MessageViewHolder) holder;
            MessageCenterEntity entity = data.get(position);
            viewHolder.title.setText(entity.getTitle());
            viewHolder.content.setText(entity.getContent());
            viewHolder.date.setText(entity.getDate().toString());
            if (entity.getIsOpen() == 1) {
                viewHolder.noOpen.setVisibility(View.GONE);
            } else {
                viewHolder.noOpen.setVisibility(View.VISIBLE);
            }
            viewHolder.view.setTag(entity.getNetId());
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, position);
                }
            });
            viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
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
