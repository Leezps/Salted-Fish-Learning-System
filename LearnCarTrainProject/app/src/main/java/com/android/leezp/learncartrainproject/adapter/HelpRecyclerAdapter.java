package com.android.leezp.learncartrainproject.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.leezp.learncartrainproject.R;
import com.android.leezp.learncartrainproject.entities.HelpEntity;
import com.android.leezp.learncartrainproject.interfaces.OnItemClickListener;

import java.util.List;

public class HelpRecyclerAdapter extends RecyclerView.Adapter {
    private List<HelpEntity> data;
    private OnItemClickListener itemClickListener;

    public HelpRecyclerAdapter(List<HelpEntity> data) {
        this.data = data;
    }

    public void addOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void notifyDataChange(List<HelpEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_help_item, parent, false);
        return new HelpRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  HelpRecyclerViewHolder) {
            HelpRecyclerViewHolder viewHolder = (HelpRecyclerViewHolder) holder;
            HelpEntity entity = data.get(position);
            viewHolder.view.setText(entity.getTitle());
            viewHolder.view.setTag(entity);
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.OnItemClick(v);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private class HelpRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView view;

        public HelpRecyclerViewHolder(View itemView) {
            super(itemView);
            view = ((TextView) itemView);
        }
    }
}
