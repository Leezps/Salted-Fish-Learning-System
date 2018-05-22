package com.android.leezp.learncartrainproject.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.leezp.learncartrainproject.GlideApp;
import com.android.leezp.learncartrainproject.R;
import com.android.leezp.learncartrainproject.entities.BookSuccessOrderAdapterEntity;
import com.android.leezp.learncartrainproject.interfaces.OnItemLongClickListener;
import com.android.leezp.learncartrainproject.utils.ProjectConstant;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookSuccessOrderRecyclerAdapter extends RecyclerView.Adapter {
    private List<BookSuccessOrderAdapterEntity> data;
    private OnItemLongClickListener itemLongClickListener;

    public BookSuccessOrderRecyclerAdapter(List<BookSuccessOrderAdapterEntity> data) {
        this.data = data;
    }

    public void notifyDataChange(List<BookSuccessOrderAdapterEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_success_order_item, parent, false);
        return new BookSuccessOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BookSuccessOrderViewHolder) {
            BookSuccessOrderViewHolder viewHolder = (BookSuccessOrderViewHolder) holder;
            BookSuccessOrderAdapterEntity entity = data.get(position);
            GlideApp.with(viewHolder.view)
                    .load(ProjectConstant.SERVER_ADDRESS+entity.getHeadUrl())
                    .placeholder(R.drawable.image_loading_black)
                    .error(R.drawable.activity_book_success_order_item_default_head)
                    .into(viewHolder.head);
            viewHolder.name.setText(entity.getName());
            viewHolder.phone.setText(entity.getPhone());
            viewHolder.timeSlot.setText(entity.getDateTimeSlot());
            viewHolder.price.setText(entity.getPrice());
            viewHolder.place.setText(entity.getPlace());
            viewHolder.view.setTag(entity.getNetId());
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

    private class BookSuccessOrderViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private CircleImageView head;
        private TextView name;
        private TextView phone;
        private TextView timeSlot;
        private TextView price;
        private TextView place;

        public BookSuccessOrderViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            head = itemView.findViewById(R.id.activity_book_success_order_item_head);
            name = itemView.findViewById(R.id.activity_book_success_order_item_name);
            phone = itemView.findViewById(R.id.activity_book_success_order_item_phone);
            timeSlot = itemView.findViewById(R.id.activity_book_success_order_item_time_slot);
            price = itemView.findViewById(R.id.activity_book_success_order_item_price);
            place = itemView.findViewById(R.id.activity_book_success_order_item_place);
        }
    }
}
