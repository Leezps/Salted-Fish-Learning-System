package com.android.leezp.learncarproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.entity.HelpEntity;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leezp on 2018/4/4.
 */

public class HelpAdapter extends RecyclerView.Adapter {
    class HelpViewHolder extends RecyclerView.ViewHolder {
        private TextView question;

        public HelpViewHolder(View itemView) {
            super(itemView);
            question = ((TextView) itemView.findViewById(R.id.fragment_help_item_question));
        }
    }

    private List<HelpEntity> data;
    private OnItemClickListener itemClickListener;

    public HelpAdapter(List<HelpEntity> data) {
        this.data = data;
    }

    public void addOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void notifyDataChange(List<HelpEntity> data) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_help_item, parent, false);
        return new HelpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof  HelpViewHolder) {
            HelpViewHolder viewHolder = (HelpViewHolder) holder;
            HelpEntity entity = data.get(position);
            viewHolder.question.setText(entity.getTitle());
            viewHolder.question.setTag(entity.getNetId());
            viewHolder.question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
