package com.android.leezp.learncarproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.leezp.learncarproject.R;

import java.util.List;

public class PublishOrderBookAdapter extends RecyclerView.Adapter {
    private List<String> data;

    public PublishOrderBookAdapter(List<String> data) {
        this.data = data;
    }

    public void notifyDataChange(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_publish_order_book_order_item, parent, false);
        return new BookOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BookOrderViewHolder) {
            ((BookOrderViewHolder) holder).timeSlot.setText(data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private class BookOrderViewHolder extends RecyclerView.ViewHolder {
        private TextView timeSlot;

        public BookOrderViewHolder(View itemView) {
            super(itemView);
            timeSlot = ((TextView) itemView);
        }
    }
}
