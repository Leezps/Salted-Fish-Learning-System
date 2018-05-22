package com.android.leezp.learncarproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.entity.ExamCheatsEntity;

import java.util.ArrayList;
import java.util.List;

public class ExamCheatsAdapter extends RecyclerView.Adapter {
    List<ExamCheatsEntity> data;

    public ExamCheatsAdapter(List<ExamCheatsEntity> data) {
        this.data = data;
    }

    public void notifyDataChange(List<ExamCheatsEntity> data) {
        if (data == null) return;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_exam_cheats_item, parent, false);
        return new CheatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  CheatsViewHolder) {
            CheatsViewHolder viewHolder = (CheatsViewHolder) holder;
            ExamCheatsEntity entity = data.get(position);
            viewHolder.content.setText(entity.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class CheatsViewHolder extends RecyclerView.ViewHolder {
        private TextView content;

        public CheatsViewHolder(View itemView) {
            super(itemView);
            content = ((TextView) itemView.findViewById(R.id.fragment_exam_cheats_item_content));
        }
    }
}
