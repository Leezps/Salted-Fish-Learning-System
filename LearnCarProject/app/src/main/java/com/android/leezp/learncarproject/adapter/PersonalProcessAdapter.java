package com.android.leezp.learncarproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.entity.DriverProcessEntity;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class PersonalProcessAdapter extends RecyclerView.Adapter {
    class PersonalProcessViewHolder extends RecyclerView.ViewHolder {
        private TextView item;

        public PersonalProcessViewHolder(View itemView) {
            super(itemView);
            item = ((TextView) itemView);
        }
    }

    private List<DriverProcessEntity> data = null;
    private OnItemClickListener itemClickListener;

    public PersonalProcessAdapter(List<DriverProcessEntity> data) {
        this.data = data;
    }

    public void addOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void notifyDataChange(List<DriverProcessEntity> data) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_personal_information_process_recycler_item, parent, false);
        return new PersonalProcessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PersonalProcessViewHolder) {
            PersonalProcessViewHolder viewHolder = (PersonalProcessViewHolder) holder;
            viewHolder.item.setText(data.get(position).getTitle());
            viewHolder.item.setTag(data.get(position).getNetId());
            viewHolder.item.setOnClickListener(new View.OnClickListener() {
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
