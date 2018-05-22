package com.android.leezp.learncarproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.entity.DriverProcessEntity;

import java.util.List;

public class DriverProcessAdapter extends RecyclerView.Adapter {
    private List<DriverProcessEntity> data;

    public DriverProcessAdapter(List<DriverProcessEntity> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_driver_process_item, parent, false);
        return new ProcessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProcessViewHolder) {
            ProcessViewHolder viewHolder = (ProcessViewHolder) holder;
            DriverProcessEntity entity = data.get(position);
            viewHolder.title.setText(entity.getTitle());
            viewHolder.content.setText(entity.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ProcessViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView content;

        public ProcessViewHolder(View itemView) {
            super(itemView);
            title = ((TextView) itemView.findViewById(R.id.fragment_driver_process_item_title));
            content = ((TextView) itemView.findViewById(R.id.fragment_driver_process_item_content));
        }

    }
}
